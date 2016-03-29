def microservices = '''
microservices {
  sensor_config {
    url = 'https://github.com/djalexd/microservices-masterpom.git'
    branch = 'master'
    module = 'sensor-config'
  }
  sensor_alerts {
    url = 'https://github.com/djalexd/microservices-masterpom.git'
    branch = 'master'
    module = 'sensor-alerts'
  }
}
'''

def slurper = new ConfigSlurper()
// fix classloader problem using ConfigSlurper in job dsl
slurper.classLoader = this.class.classLoader
def config = slurper.parse(microservices)

// create job for every microservice
config.microservices.each { name, data ->
  createBuildJob(name,data)
}


def createBuildJob(name,data) {
  
  freeStyleJob("${name}-build") {
  
    scm {
      git {
        remote {
          url(data.url)
        }
        branch(data.branch)
        createTag(false)
      }
    }
  
    triggers {
      scm('H/15 * * * *')
    }

    steps {
      maven {
        if (data.module) {
          goals('-pl ' + data.module + ' clean install')
        } else {
          goals('clean install')
        }
      }
    }

  }

}
