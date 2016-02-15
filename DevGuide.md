# Developer's Guide #

This is a short guide for those who want to start developing the game.

## Quick Start Guide ##

Once you have checked out the project from the svn repository, put all the _JAR_ files from the _lib_ directory in your project's classpath. Run _JToyRacing_ passing _-Djava.library.path=lib_ as a virtual machine argument.

## Package and directory organization ##

The source packages are organized as follows:
  * action _(general actions)_
  * audio _(regarding audio manipulation)_
  * core _(control classes)_
    * hud _(screen information)_
  * entity _(main elements)_
  * util _(utilities)_

The resources used by the game are:
  * audio _(audio files)_
  * images _(image files)__* models_(3D model files)_* texture_(texture files)

The _dist_ directory is where the build puts the JAR and all the files to distribution.

The _executables_ directory has script files to execute the JAR dependending on the operating system.

The _lib_ directory has all the libraries required by the game.

## Checkstyle ##

The source code follows a set of ckeckstyle rules defined by the file _jtoyracing-checks.xml_ which has most of the Sun coding conventions.

## Ant/Maven ##

Although the directory organization seems like Maven's directory layout, it is not used but I have plans to use it in the future. The project tasks are done by an Ant scrit called _build.xml_.

The Ant tasks named dist\_x, where x is the target operating system, create a JAR and copy all the necessary files to _dist_ directory. Files with _so_ extension are used in linux version, _dll_ extension are used in windows.