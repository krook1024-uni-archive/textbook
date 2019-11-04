#!/usr/bin/env python2
# TensorFlow Hello World 1!
# twicetwo.py
#
import tensorflow as tf

node1 = tf.constant(2)
node2 = tf.constant(2)

node_twicetwo = tf.math.multiply(node1, node2, name="twicetwo")

sess = tf.Session()
print sess.run(node_twicetwo)

writer = tf.summary.FileWriter("/tmp/twicetwo", sess.graph)
# nbatfai@robopsy:~/Robopsychology/repos/tf/tf/tensorboard$ python tensorboard.py --logdir=/tmp/twicetwo

tf.train.write_graph(sess.graph_def, "models/", "twicetwo.pb", as_text=False)
