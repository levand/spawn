(ns leiningen.spawn
  "A code generation utility"
  (:require [leiningen.core])
  (:import (java.io File)))

(defn resolve-genome
  "Loads a Spawn genome, assuming a leiningen.spawn.genome.<name> namespace.
Returns the var <name> in the genome namespace."
  [name]
  (try
    (let [namespace (symbol (str (symbol (str "leiningen.spawn.genome." name))))]
      (require namespace)
      (ns-resolve namespace (symbol name)))
    (catch Exception e
      (println "Error loading genome")
      (throw e))))

(defn- requires-project?
  "Tests if a given genome var requires a project to run"
  [genome]
  (every? (fn [arglist] (= (symbol "project") (first arglist)))
          (:arglists (meta genome))))

(defn- takes-project?
  "Tests if a given genome var can take a project"
  [genome]
  (some (fn [arglist] (= (symbol "project") (first arglist)))
          (:arglists (meta genome))))

(defn- context-project
  "Gets the project in which Spawn is being run, or nil if it's being run outside a project"
  []
  (if (.exists (File. "project.clj")) (leiningen.core/read-project)))

(defn spawn
  "A code generation utility"
  [& args]
  (let [project (context-project)
        genome (resolve-genome (first args))
        genome-fn (deref genome)
        arguments (rest args)]
    (if project
      (if (takes-project? genome)
        (apply genome-fn project arguments)
        (apply genome-fn arguments))
      (if (requires-project? genome)
        (println "Error: genome" (first args) "must be run in the context of an existing project")
        (apply genome-fn arguments)))))


