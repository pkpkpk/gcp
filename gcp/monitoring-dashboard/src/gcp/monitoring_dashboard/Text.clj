(ns gcp.monitoring-dashboard.Text
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 Text Text$Format Text$TextStyle Text$TextStyle$HorizontalAlignment Text$TextStyle$VerticalAlignment Text$TextStyle$PaddingSize Text$TextStyle$FontSize Text$TextStyle$PointerLocation)))

(def Text$TextStyle:schema
  (g/schema
   [:map
    [:backgroundColor {:optional true} :string]
    [:textColor {:optional true} :string]
    [:horizontalAlignment {:optional true} [:enum "HORIZONTAL_ALIGNMENT_UNSPECIFIED" "H_LEFT" "H_CENTER" "H_RIGHT"]]
    [:verticalAlignment {:optional true} [:enum "VERTICAL_ALIGNMENT_UNSPECIFIED" "V_TOP" "V_CENTER" "V_BOTTOM"]]
    [:padding {:optional true} [:enum "PADDING_SIZE_UNSPECIFIED" "P_EXTRA_SMALL" "P_SMALL" "P_MEDIUM" "P_LARGE" "P_EXTRA_LARGE"]]
    [:fontSize {:optional true} [:enum "FONT_SIZE_UNSPECIFIED" "FS_EXTRA_SMALL" "FS_SMALL" "FS_MEDIUM" "FS_LARGE" "FS_EXTRA_LARGE"]]
    [:pointerLocation {:optional true} [:enum "POINTER_LOCATION_UNSPECIFIED" "PL_TOP" "PL_RIGHT" "PL_BOTTOM" "PL_LEFT" "PL_TOP_LEFT" "PL_TOP_RIGHT" "PL_RIGHT_TOP" "PL_RIGHT_BOTTOM" "PL_BOTTOM_RIGHT" "PL_BOTTOM_LEFT" "PL_LEFT_BOTTOM" "PL_LEFT_TOP"]]]))

(defn Text$TextStyle:from-edn
  [{:keys [backgroundColor textColor horizontalAlignment verticalAlignment padding fontSize pointerLocation] :as arg}]
  (g/strict! Text$TextStyle:schema arg)
  (let [builder (doto (Text$TextStyle/newBuilder)
                  (cond->
                   backgroundColor (.setBackgroundColor backgroundColor)
                   textColor (.setTextColor textColor)))]
    (some->> horizontalAlignment (Text$TextStyle$HorizontalAlignment/valueOf) (.setHorizontalAlignment builder))
    (some->> verticalAlignment (Text$TextStyle$VerticalAlignment/valueOf) (.setVerticalAlignment builder))
    (some->> padding (Text$TextStyle$PaddingSize/valueOf) (.setPadding builder))
    (some->> fontSize (Text$TextStyle$FontSize/valueOf) (.setFontSize builder))
    (some->> pointerLocation (Text$TextStyle$PointerLocation/valueOf) (.setPointerLocation builder))
    (.build builder)))

(defn Text$TextStyle:to-edn
  [^Text$TextStyle arg]
  (cond-> {}
          (not-empty (.getBackgroundColor arg)) (assoc :backgroundColor (.getBackgroundColor arg))
          (not-empty (.getTextColor arg)) (assoc :textColor (.getTextColor arg))
          (not= "HORIZONTAL_ALIGNMENT_UNSPECIFIED" (.name (.getHorizontalAlignment arg))) (assoc :horizontalAlignment (.name (.getHorizontalAlignment arg)))
          (not= "VERTICAL_ALIGNMENT_UNSPECIFIED" (.name (.getVerticalAlignment arg))) (assoc :verticalAlignment (.name (.getVerticalAlignment arg)))
          (not= "PADDING_SIZE_UNSPECIFIED" (.name (.getPadding arg))) (assoc :padding (.name (.getPadding arg)))
          (not= "FONT_SIZE_UNSPECIFIED" (.name (.getFontSize arg))) (assoc :fontSize (.name (.getFontSize arg)))
          (not= "POINTER_LOCATION_UNSPECIFIED" (.name (.getPointerLocation arg))) (assoc :pointerLocation (.name (.getPointerLocation arg)))))

(def schema
  (g/schema
   [:map
    [:content {:optional true} :string]
    [:format {:optional true} [:enum "FORMAT_UNSPECIFIED" "MARKDOWN" "RAW"]]
    [:style {:optional true} Text$TextStyle:schema]]))

(defn from-edn
  [{:keys [content format style] :as arg}]
  (g/strict! schema arg)
  (let [builder (doto (Text/newBuilder)
                  (cond->
                   content (.setContent content)
                   style (.setStyle (Text$TextStyle:from-edn style))))]
    (some->> format (Text$Format/valueOf) (.setFormat builder))
    (.build builder)))

(defn to-edn
  [^Text arg]
  (cond-> {}
          (not-empty (.getContent arg)) (assoc :content (.getContent arg))
          (not= "FORMAT_UNSPECIFIED" (.name (.getFormat arg))) (assoc :format (.name (.getFormat arg)))
          (.hasStyle arg) (assoc :style (Text$TextStyle:to-edn (.getStyle arg)))))
