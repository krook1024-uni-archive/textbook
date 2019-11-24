import tensorflow as tf
(x_train, y_train), (x_test, y_test) = tf.keras.datasets.mnist.load_data()

import matplotlib
import matplotlib.pyplot as plt
matplotlib.use('TkAgg')

def read_img():
    import numpy as np
    img = load_img("sajat8a.png", target_size=(28, 28), color_mode="grayscale")
    x = img_to_array(img)
    return x

print()
print('Tanitas folyamatban...')

# Reshaping the array to 4-dims so that it can work with the Keras API
x_train = x_train.reshape(x_train.shape[0], 28, 28, 1)
x_test = x_test.reshape(x_test.shape[0], 28, 28, 1)
input_shape = (28, 28, 1)

# Making sure that the values are float so that we can get decimal points after division
x_train = x_train.astype('float32')
x_test = x_test.astype('float32')

# Normalizing the RGB codes by dividing it to the max RGB value.
x_train /= 255
x_test /= 255

# Importing the required Keras modules containing model and layers
from keras.models import Sequential
from keras.layers import Dense, Conv2D, Dropout, Flatten, MaxPooling2D
from keras.preprocessing.image import load_img, img_to_array
from keras.applications.resnet50 import preprocess_input
from PIL import Image

# Creating a Sequential Model and adding the layers
model = Sequential()
model.add(Conv2D(28, kernel_size=(3,3), input_shape=input_shape))
model.add(MaxPooling2D(pool_size=(2, 2)))
model.add(Flatten()) # Flattening the 2D arrays for fully connected layers
model.add(Dense(128, activation=tf.nn.relu))
model.add(Dropout(0.2))
model.add(Dense(10, activation=tf.nn.softmax))

model.compile(optimizer='adam',
              loss='sparse_categorical_crossentropy',
              metrics=['accuracy'])

model.fit(x=x_train,y=y_train, epochs=10)

model.evaluate(x_test, y_test)

# 42
print()
print("mutatom a 42. elemet, folytatáshoz zárd be az ablakot")

plt.imshow(x_test[42].reshape(28, 28),cmap='Greys')
plt.savefig("42.png")
plt.show()

pred = model.predict(x_test[42].reshape(1, 28, 28, 1))
print("ennek ismerte fel:", pred.argmax())

# saját kép
print()
print("mutatom a saját kézi rajzot, folytatáshoz zárd be az ablakot")

image = read_img()
plt.imshow(image.reshape(28, 28), cmap='Greys')
plt.savefig("saj8.png")
plt.show()

pred = model.predict(image.reshape(1, 28, 28, 1))
print("ennek ismerte fel:", pred.argmax())
