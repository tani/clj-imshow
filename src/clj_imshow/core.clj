(ns clj-imshow.core
  (:import [org.bytedeco.javacpp opencv_imgcodecs]
           [org.opencv.core Mat MatOfByte]
           [org.opencv.imgcodecs Imgcodecs]
           [java.io ByteArrayInputStream]
           [javax.imageio ImageIO])
  (:use seesaw.core))

(def imlabel (label))
(def imframe (frame :content imlabel :on-close :dispose))

(defn- toBufferedImage [image]
  (if (instance? Mat image)
    ;; OpenCV
    (let [mob (MatOfByte.)]
       (Imgcodecs/imencode ".png" image mob)
       (-> mob .toArray ByteArrayInputStream. ImageIO/read))
    ;; JavaCV
    (let [ba (byte-array (* (.channels image) (.cols image) (.rows image)))]
      (opencv_imgcodecs/imencode ".png" image ba)
      (-> ba ByteArrayInputStream. ImageIO/read))))

(defn imshow
  ([image]
   (imshow "Untitled" image))
  ([title image]
   (let [bi (toBufferedImage image)]
     (if (.isShowing imframe)
       (do (.setTitle imframe title)
           (.setIcon imlabel (icon bi)))
       (do (.setIcon imlabel (icon bi))
           (.setTitle imframe title)
           (invoke-later (-> imframe pack! show!)))))))
  
