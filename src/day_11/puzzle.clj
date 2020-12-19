(ns day-11.puzzle
  (:require [clojure.string        :as cstr]
            [santas-little-helpers :as h]))

(def test-seat-map [["L" "." "L" "L" "." "L" "L" "." "L" "L"]
                    ["L" "L" "L" "L" "L" "L" "L" "." "L" "L"]
                    ["L" "." "L" "." "L" "." "." "L" "." "."]
                    ["L" "L" "L" "L" "." "L" "L" "." "L" "L"]
                    ["L" "." "L" "L" "." "L" "L" "." "L" "L"]
                    ["L" "." "L" "L" "L" "L" "L" "." "L" "L"]
                    ["." "." "L" "." "L" "." "." "." "." "."]
                    ["L" "L" "L" "L" "L" "L" "L" "L" "L" "L"]
                    ["L" "." "L" "L" "L" "L" "L" "L" "." "L"]
                    ["L" "." "L" "L" "L" "L" "L" "." "L" "L"]])


(def input (->> (h/read-input "data/day_11/input.txt")
                (mapv #(cstr/split % #""))))

(defn get-seats-in-direction
  [first-neighbour current-seat-pos seat-map]
  (let [seat (get-in seat-map first-neighbour)]
    (cond
      (nil? seat)  nil
      (= seat "L") "L"
      (= seat "#") "#"
      (= seat ".") (let [[rise run]    [(- (first first-neighbour) (first current-seat-pos))
                                        (- (last first-neighbour) (last current-seat-pos))]
                         apply-slope   (fn [[y x]]
                                         [(+ y rise) (+ x run)])
                         next-adjacent (loop [next-neighbour-pos (apply-slope first-neighbour)]
                                         (let [next-neighbour (get-in seat-map next-neighbour-pos)]
                                           (cond
                                             (nil? next-neighbour)  "."
                                             (= next-neighbour "#") "#"
                                             (= next-neighbour "L") "L"
                                             (= next-neighbour ".") (recur (apply-slope next-neighbour-pos)))))]
                     next-adjacent))))

(defn get-neighbour-positions
  [position]
  (let [[row-n seat-n] position]
    (remove #{position} (for [y (range (dec row-n) (+ row-n 2))
                              x (range (dec seat-n) (+ seat-n 2))]
                          [y x]))))

(defn update-seat
  [current-seat neighbours]
  (let [is-aisle               (= current-seat ".")
        is-empty               (= current-seat "L")
        occupied-count         (count (filter #{"#"} neighbours))
        no-occupied-seats      (zero? occupied-count)
        is-occupied            (= current-seat "#")
        four-or-more           (>= occupied-count 4)
        #_five-or-more       #_(>= occupied-count 5)] ;; <-- replace four-or-more with this for pt 2
    (cond
      is-aisle                               "."
      (and is-empty no-occupied-seats)       "#"
      (and is-empty (not no-occupied-seats)) "L"
      (and is-occupied four-or-more)         "L"
      (and is-occupied (not four-or-more))   "#")))

(defn next-state-at-position
  [current-seat-pos seat-map]
  (let [neighbour-positions (get-neighbour-positions current-seat-pos)
        neighbours          (map #(get-in seat-map %) neighbour-positions) ;; (map #(get-seats-in-direction % current-seat-pos seat-map) neighbour-positions) for pt 2
        current-seat        (get-in seat-map current-seat-pos)]
    (update-seat current-seat neighbours)))

(defn generate-new-seat-map
  [prev-seat-map]
  (let [row-count  (count prev-seat-map)
        seat-count (count (first prev-seat-map))
        positions  (for [y (range row-count)
                         x (range seat-count)]
                     [y x])]
    (->> positions
         (map #(next-state-at-position % prev-seat-map))
         (partition seat-count)
         (mapv vec))))

(defn find-occupied-seats
  [input]
  (loop [seat-map input]
    (let [next-state (generate-new-seat-map seat-map)]
      (if (= next-state seat-map)
        (->> seat-map
             (flatten)
             (filter #{"#"})
             (count))
        (recur next-state)))))

(comment
  #_(time (find-occupied-seats input))
  (find-occupied-seats test-seat-map))







