# Copyright 2015 The TensorFlow Authors. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ==============================================================================

# Norbert Batfai, 27 Nov 2016
# Some modifications and additions to the original code:
# https://github.com/tensorflow/tensorflow/blob/r0.11/tensorflow/examples/tutorials/mnist/mnist_softmax.py
# See also http://progpater.blog.hu/2016/11/13/hello_samu_a_tensorflow-bol
# ==============================================================================

"""A very simple MNIST classifier.

See extensive documentation at
http://tensorflow.org/tutorials/mnist/beginners/index.md
"""
from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

# Import data
from tensorflow.examples.tutorials.mnist import input_data

import tensorflow as tf
tf.logging.set_verbosity(tf.logging.ERROR) # Shut TF up

import matplotlib
import matplotlib.pyplot as plt
matplotlib.use('TkAgg') # sudo apt install python3-tk

def readimg():
    file = tf.read_file("sajat8a.png")
    img = tf.image.decode_png(file,channels=1)
    return img

def main(_):
    data_dir = '/tmp/tensorflow/mnist/input_data'
    mnist = input_data.read_data_sets(data_dir, one_hot=True)

    # Create the model
    x = tf.placeholder(tf.float32, [None, 784])
    W = tf.Variable(tf.zeros([784, 10]))
    b = tf.Variable(tf.zeros([10]))
    y = tf.matmul(x, W) + b

    # Define loss and optimizer
    y_ = tf.placeholder(tf.float32, [None, 10])

    # The raw formulation of cross-entropy,
    #
    #   tf.reduce_mean(-tf.reduce_sum(y_ * tf.log(tf.nn.softmax(y)),
    #                                 reduction_indices=[1]))
    #
    # can be numerically unstable.
    #
    # So here we use tf.nn.softmax_cross_entropy_with_logits on the raw
    # outputs of 'y', and then average across the batch.
    cross_entropy = tf.reduce_mean(
        tf.nn.softmax_cross_entropy_with_logits(labels = y_, logits = y)
    )

    train_step = tf.train.GradientDescentOptimizer(0.5).minimize(cross_entropy)

    sess = tf.InteractiveSession()
    # Train
    tf.initialize_all_variables().run(session=sess)
    print()
    print("> A halozat tanitasa")
    for i in range(1000):
        batch_xs, batch_ys = mnist.train.next_batch(100)
        sess.run(train_step, feed_dict={x: batch_xs, y_: batch_ys})
        if i % 100 == 0:
            print("->", i/10, "%")
    print()

    # Test trained model
    print("> A halozat tesztelese")
    correct_prediction = tf.equal(tf.argmax(y, 1), tf.argmax(y_, 1))
    accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
    print("-> Pontossag: ", sess.run(accuracy, feed_dict={x: mnist.test.images,
      y_: mnist.test.labels}))
    print()

    print("> A MNIST 42. tesztkepenek felismerese, mutatom a szamot, a"
          " tovabblepeshez csukd be az ablakot")

    img = mnist.test.images[42]
    image = img

    plt.imshow(image.reshape(28, 28), cmap=plt.cm.binary)
    plt.savefig("4.png")
    plt.show()

    classification = sess.run(tf.argmax(y, 1), feed_dict={x: [image]})

    print("-> Ezt a halozat ennek ismeri fel: ", classification[0])
    print()

    print("> A sajat kezi 8-asom felismerese, mutatom a szamot, a"
          " tovabblepeshez csukd be az ablakot")
    img = readimg()
    image = img.eval()
    image = image.reshape(28*28)
    plt.imshow(image.reshape(28,28), cmap=plt.cm.binary)
    plt.savefig("saj8.png")
    plt.show()

    classification = sess.run(tf.argmax(y, 1), feed_dict={x: [image]})

    print("-> Ezt a halozat ennek ismeri fel: ", classification[0])
    print()

if __name__ == '__main__':
    tf.app.run()
