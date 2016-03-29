freeStyleJob('order-build') {
	scm {
		git {
			remote {
				url('https://github.com/ralfstuckert/jobdsl-sample.git')
			}
			branch('order')
			createTag(false)
		}
	}
	triggers {
		scm('H/15 * * * *')
	}

	steps {
		maven {
			mavenInstallation('3.1.1')
			goals('clean install')
		}
	}
}
