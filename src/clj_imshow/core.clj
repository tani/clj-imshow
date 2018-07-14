(ns clj-imshow.core
  (:import [org.bytedeco.javacpp opencv_imgcodecs]
           [java.io ByteArrayInputStream]
           [javax.imageio ImageIO])
  (:use seesaw.core))

(def imlabel (label))
(def imframe (frame :content imlabel :on-close :dispose))

(defn imshow
  ([image]
   (imshow "Untitled" image))
  ([title image]
   (let [ba (byte-array (* (.channels image) (.cols image) (.rows image)))]
     (opencv_imgcodecs/imencode ".png" image ba)
     (let [bi (-> ba ByteArrayInputStream. ImageIO/read)]
       (if (.isShowing imframe)
         (do (.setTitle imframe title)
             (.setIcon imlabel (icon bi)))
         (do (.setIcon imlabel (icon bi))
             (.setTitle imframe title)
             (invoke-later (-> imframe pack! show!))))))))
