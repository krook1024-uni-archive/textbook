(defun factorial (N)
       (let ((R 1))
            (do ((i 1 (+ i 1) )) ((> i N) R)
                (setf r (* r i))
            )
       )
)

(print (factorial 5))
