(ns {{name}}.server
  (:use compojure.core ring.adapter.jetty)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [{{name}}.controllers.index :as index]))
 
(defroutes root-routes
  "Defines the basic set of routes used by the application"
  (GET "/" [] index/index-routes)
  (route/files "/public" {:root "public"})
  (route/not-found "<h1>Page not found. Sorry.</h1>"))

(defn wrap-middleware
  "Wraps the given routes in the application's middleware"
  [routes]
  (handler/site routes))

(defn -main
  "Start up a basic Jetty server"
  [& args]
  (println "Starting Jetty on port 8080...")
  (run-jetty (wrap-middleware root-routes) {:port 8080}))