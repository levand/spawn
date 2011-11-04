(ns spawn.core
  "A code generation utility"
  (:import (java.io File)))

(defn resolve-genome
  "Loads a Spawn genome, assuming a .spawn.genome.<name> namespace.
Returns the var <name> in the genome namespace."
  [name]
  (try
    (let [namespace (symbol (str (symbol (str "spawn.genome." name))))]
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

(defn spawn
  "A code generation utility"
  [genome & {:keys [project args]}]
  (let [genome (resolve-genome genome)
        genome-fn (deref genome)]
    (if project
      (if (takes-project? genome)
        (apply genome-fn project args)
        (apply genome-fn args))
      (if (requires-project? genome)
        (throw (Exception. "genome" "must be run in the context of an existing project"))
        (apply genome-fn args)))))


