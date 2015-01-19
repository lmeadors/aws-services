jackson-env
===

This is a simplified API for AWS that I use in several projects - it's shared here to make it
easier to manage updates to it in the various projects that use it.

It expects to be injected with a working `AmazonCloudWatch` object as well as an environment name
("dev", "test", etc...) and a namespace value (typically something that identifies your project).

Maven
---

To add this to your project, add this:

	<dependencies>
		<dependency>
			<groupId>com.elmsoftware</groupId>
			<artifactId>aws-services</artifactId>
			<version>1.0-SNAPSHOT</version>
		</dependency>
	</dependencies>

It's not in the central repositories, so you also have to add this:

	<repositories>
		<repository>
			<id>elm-software-releases</id>
			<url>https://github.com/lmeadors/maven-repo/raw/master/releases</url>
		</repository>
	</repositories>

If you want to use the latest snapshot, there is a snapshot repository, too:

	<repositories>
		<repository>
			<id>elm-software-releases</id>
			<url>https://github.com/lmeadors/maven-repo/raw/master/releases</url>
		</repository>
		<repository>
			<id>elm-software-snapshots</id>
			<url>https://github.com/lmeadors/maven-repo/raw/master/snapshots</url>
		</repository>
	</repositories>

