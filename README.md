Spawn
=====

Warning: Spawn is still a work in progress and is not yet ready for public consumption.

Spawn is a leiningen plugin that makes it easy to get up and running with various types of Clojure projects. It supports automated project and code generation, similar to the scaffolding and project generation features found in Rails.

It has two primary goals:
1. To provide templated projects as a learning tool for Clojure newbies, allowing them to start building applications (such as web apps) right away. Figuring out how to assemble the relevant components can be a daunting task for someone lacking prior Clojure experience. 
2. To save time for experienced Clojure users, allowing them to stub out common application types in seconds instead of having to tediously build out the same infrastructure every time.

Getting Started
---------------
1. [Install Leiningen](https://github.com/technomancy/leiningen)
2. Install the Spawn plugin by typing `lein plugin install spawn 0.1.0` at a command prompt. (NOTE: This will not work until spawn is mature enough for release and has been pushed to Clojars. If you're interested in trying out Spawn in the meantime, just clone this repository and run `lein install` first.

Creating a Web Application
--------------------------
1. At the commandline, type `lein spawn webapp <name>`, substituting a name of your choice. This will create a new directory at your current location.
2. Switch to the directory that was just created, and type `lein run`
3. That's it! You've successfully created a web application and started a server. You can visit `localhost:8080` in your browser to check it out.

Extensions
----------
Spawn is highly extensible, making it easy to write additional templates or recipes, called "genomes." 

To create a Spawn genome, just create a normal Clojure source file to the src path, for example, `/leiningen/spawn/genome/mygenome.clj`. This file should contain a public function of the same name as the file's namespace. e.g, `mygenome`. Like leiningen plugins, if the first parameter to the genome function is called 'project', a leiningen project will be passed in if `lein` is invoked from within a leiningen project folder.

Then, after rebuilding the Spawn plugin, you can invoke your function from the command line using `lein spawn`: for example, `lein spawn mygenome`. Any additional arguments after the genome name will be passed as arguments to the genome's clojure function.

See the `webapp` genome for an example of some of the utilities Spawn provides for generating directory structures and rendering templates using the StringTemplate library.
  
TODO
----

Immediate needs before public release:

* Implement a genome for controller generation within a webapp
* Implement a genome for view generation within a webapp (using StringTemplate)
* Make more newbie-friendly by providing helpful guidance on errors instead of stack traces.
* Provide a mechanism for introspecting and listing the available genomes, along with documentation

Nice-to-haves:

* Implement a genome for RESTful resource generation within a webapp
* Implement a genome for generating an Enlive view
* Implement a a genome for generating a Hiccup view
* Enable war compilation for the generated webapp
* Make it possible to create and install genomes without recompiling and redistributing Spawn

