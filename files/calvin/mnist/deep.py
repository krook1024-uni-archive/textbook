from tensorflow.examples.tutorials.mnist import input_data
mnist = input_data.read_data_sets('MNIST_data', one_hot=True)

import tensorflow as tf
sess = tf.InteractiveSession()

import matplotlib
import matplotlib.pyplot as plt
matplotlib.use('TkAgg') # sudo apt install python3-tk

# Sajat kepet beolvaso fv.
def readimg():
    file = tf.read_file("sajat8a.png")
    img = tf.image.decode_png(file,channels=1)
    return img

# Pontossag szamolo fv.
def compute_accuracy(v_xs, v_ys):
    global y_conv
    y_pre = sess.run(y_conv, feed_dict={x: v_xs, keep_prob: 1})
    correct_prediction = tf.equal(tf.argmax(y_pre,1), tf.argmax(v_ys,1))
    accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
    result = sess.run(accuracy, feed_dict={x: v_xs, y_: v_ys, keep_prob: 1})
    return result

# Sulyok, biasok
def weight_variable(shape):
  initial = tf.truncated_normal(shape, stddev=0.1)
  return tf.Variable(initial)

def bias_variable(shape):
  initial = tf.constant(0.1, shape=shape)
  return tf.Variable(initial)

# Konvolucio, pooling
def conv2d(x, W):
    return tf.nn.conv2d(x, W, strides=[1, 1, 1, 1], padding='SAME')

def max_pool_2x2(x):
    return tf.nn.max_pool(x, ksize=[1, 2, 2, 1],
            strides=[1, 2, 2, 1], padding='SAME')

x = tf.placeholder(tf.float32, shape=[None, 784])
y_ = tf.placeholder(tf.float32, shape=[None, 10])

# Elso konvolucios reteg (fc1)

W_conv1 = weight_variable([5, 5, 1, 32])
b_conv1 = bias_variable([32])

x_image = tf.reshape(x, [-1,28,28,1])

h_conv1 = tf.nn.relu(conv2d(x_image, W_conv1) + b_conv1)
h_pool1 = max_pool_2x2(h_conv1)

# Masodik konvolucios reteg (fc2) -- readout

W_conv2 = weight_variable([5, 5, 32, 64])
b_conv2 = bias_variable([64])

h_conv2 = tf.nn.relu(conv2d(h_pool1, W_conv2) + b_conv2)
h_pool2 = max_pool_2x2(h_conv2)

# Elso osszevono reteg
W_fc1 = weight_variable([7 * 7 * 64, 1024])
b_fc1 = bias_variable([1024])

h_pool2_flat = tf.reshape(h_pool2, [-1, 7*7*64])
h_fc1 = tf.nn.relu(tf.matmul(h_pool2_flat, W_fc1) + b_fc1)

# Dropout reteg
keep_prob = tf.placeholder(tf.float32)
h_fc1_drop = tf.nn.dropout(h_fc1, keep_prob)

# Masodik osszevono  réteg
W_fc2 = weight_variable([1024, 10])
b_fc2 = bias_variable([10])

y_conv=tf.nn.softmax(tf.matmul(h_fc1_drop, W_fc2) + b_fc2)

sess.run(tf.initialize_all_variables())

# Tanitas
print('Tanitas folyamatban...')

cross_entropy = tf.reduce_mean(
        -tf.reduce_sum(y_ * tf.log(y_conv), reduction_indices=[1])
    )

train_step = tf.train.AdamOptimizer(1e-4).minimize(cross_entropy)

correct_prediction = tf.equal(tf.argmax(y_conv,1), tf.argmax(y_,1))

accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))

sess.run(tf.initialize_all_variables())

batchSize = 50
for i in range(100):
    batch = mnist.train.next_batch(batchSize)
    if (i % batchSize == 0):
        train_accuracy = accuracy.eval(feed_dict={
            x:batch[0], y_: batch[1], keep_prob: 1.0})
        print("step %d, training accuracy %g"%(i, train_accuracy))
    train_step.run(feed_dict={x: batch[0], y_: batch[1], keep_prob: 0.5})

# Kiertekeles + teszt

print()
print("Teszt folyamatban...")
correct_prediction = tf.equal(tf.argmax(y_conv,1), tf.argmax(y_,1))
batch_test = mnist.test.next_batch(500)
print("Pontossag:", compute_accuracy(batch_test[0], batch_test[1]))

# 42. elem
correct_prediction = tf.equal(tf.argmax(y_,1), y_)
print()
print("42. elemet mutatom, zard be a tovabblepeshez")
img = mnist.test.images[42]
image = img

#plt.imshow(image.reshape(28, 28), cmap=plt.cm.binary)
#plt.savefig("4.png")
#plt.show()

classification = sess.run(correct_prediction, feed_dict={x: [image]})

print("A halozat ennek ismerte fel:", classification[0])

"""
# Sajat kezzel
print()
print("Sajat kezi rajzot mutatom, zard be a tovabblepeshez")
img = readimg()
images = imag.eval()
image = image.reshape(28*28)

#plt.imshow(image.reshape(28, 28), cmap=plt.cm.binary)
#plt.savefig("saj8.png")
#plt.show()

classification = sess.run(tf.argmax(y_conv, 1), feed_dict={x: [image]})

print("A halozat ennek ismerte fel:", classification[0])
"""
