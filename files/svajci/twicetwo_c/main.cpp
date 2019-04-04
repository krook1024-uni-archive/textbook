//
// TensorFlow Hello World 2!
// linreg.cpp
//
// nbatfai@robopsy:~/Robopsychology/repos/tensorflow/tensorflow/linreg$ python linreg.py
// nbatfai@robopsy:~/Robopsychology/repos/tensorflow/tensorflow/tensorboard$ python tensorboard.py --logdir=/tmp/linreg
// nbatfai@robopsy:~/Robopsychology/repos/tensorflow/tensorflow/linreg$ bazel build :linreg
// nbatfai@robopsy:~/Robopsychology/repos/tensorflow/bazel-bin/tensorflow/linreg$ cp -r ~/Robopsychology/repos/tensorflow/tensorflow/linreg/models .
// nbatfai@robopsy:~/Robopsychology/repos/tensorflow/bazel-bin/tensorflow/linreg$ ./linreg
//
#include "tensorflow/core/platform/env.h"
#include "tensorflow/core/public/session.h"

int main(int argc, char **argv)
{
    tensorflow::Session *session;
    tensorflow::Status status = tensorflow::NewSession(tensorflow::SessionOptions(), &session);
    if (!status.ok()) {
        std::cout << "NewSession: " << status.ToString() << std::endl;
        return -1;
    }

    tensorflow::GraphDef graphDef;
    status = tensorflow::ReadBinaryProto(tensorflow::Env::Default(), "models/linreg.pb", &graphDef);
    if (!status.ok()) {
        std::cout << "ReadBinaryProto: " << status.ToString() << std::endl;
        return -2;
    }

    // nbatfai@robopsy:~/Robopsychology/repos/tensorflow/bazel-bin/tensorflow/linreg$ cp -r ~/Robopsychology/repos/tensorflow/tensorflow/linreg/models .

    status = session->Create(graphDef);
    if (!status.ok()) {
        std::cout << "Create: " << status.ToString() << std::endl;
        return -3;
    }

    std::vector<tensorflow::Tensor> out;

    status = session->Run( {}, {"W", "b"}, {}, &out);

    if (!status.ok()) {
        std::cout << "Run: " << status.ToString() << std::endl;
        return -4;
    }

    std::cout << out[0].scalar<float>() << std::endl;
    std::cout << out[1].scalar<float>() << std::endl;

    // nbatfai@robopsy:~/Robopsychology/repos/tensorflow/bazel-bin/tensorflow/linreg$ ./linreg

    session->Close();
    return 0;
}
