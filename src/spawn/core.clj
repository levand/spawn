(ns spawn.core
  "A code generation utility"
  (:require [clojure.java.io :as io]))

(defn resolve-genome
  "Loads a Spawn genome, assuming a .spawn.genome.<name> namespace.
Returns the var <name> in the genome namespace."
  [name]
  (let [namespace (symbol (str (symbol (str "spawn.genome." name))))]
    (require namespace)
    (ns-resolve namespace (symbol name))))

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
        (throw
         (Exception. (str "genome" genome "must be run in the context of an existing project")))
        (apply genome-fn args)))))

(defn- get-genome-files []
  (remove #(.isDirectory %)
          (.listFiles (io/file (io/resource "spawn/genome")))))

(defn- drop-ext [f]
  (.replace (first (.split f "\\.")) "_" "-"))

(defn genomes
  "Get a list of genomes on the classpath."
  []
  (for [genome (map (comp meta resolve-genome drop-ext #(.getName %))
                    (get-genome-files))]
    [(:name genome) genome]))
