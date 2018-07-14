(ns clj-imshow.core
  (:import [org.bytedeco.javacpp opencv_imgcodecs]
           [java.io ByteArrayInputStream]
           [javax.imageio ImageIO])
  (:use seesaw.core))

(defn imshow
  ([image]
   (imshow "Untitled" image))
  ([title image]
   (let [ba (byte-array (* (.channels image) (.cols image) (.rows image)))]
     (opencv_imgcodecs/imencode ".png" image ba)
     (let [bi (-> ba ByteArrayInputStream. ImageIO/read)]
       (invoke-later
        (-> (frame :title title
                   :content (label :icon bi)
                   :on-close :dispose)
            pack!
            show!))))))
