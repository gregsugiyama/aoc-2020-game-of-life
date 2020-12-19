(ns day-11.quil-puzzle
  (:require [day-11.puzzle   :as rules]
            [quil.core       :as quil]
            [quil.middleware :as qm]))

(def world
  day-11.puzzle/input
  #_rules/test-seat-map)

(def world-y
  (count world))

(def world-x
  (count (first world)))

(defn setup
  []
  (quil/frame-rate 5)
  (quil/color-mode :rgb)
  world)

(defn update-state
  [state]
  (rules/generate-new-seat-map state))

(defn draw-state
  [state]
  (quil/background 0)
  (quil/no-stroke)
  (doseq [x (range 0 world-y)]
    (doseq [y (range 0 world-x)]
      (cond
        (= (get-in state [y x]) "#") (do (quil/fill 250 0 0)
                                         (quil/rect (* x 10) (* y 10) 10 10))
        (= (get-in state [y x]) "L") (do (quil/fill 0 250 0)
                                         (quil/rect (* x 10) (* y 10) 10 10))
        (= (get-in state [y x]) ".") (do (quil/fill 0 0 250)
                                         (quil/rect (* x 10) (* y 10) 10 10))))))

(quil/defsketch aoc-11-12-2020
  :title "AOC 'Game of Life'"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [qm/fun-mode])
