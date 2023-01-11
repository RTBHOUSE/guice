docker run -v "$(pwd)":/guice --name guice-builder -it ubuntu_bazelisk:1.0.0 /bin/bash -c "cd guice && /bazelisk-linux-amd64 build //extensions/servlet/src/com/google/inject/servlet:*"
docker cp guice-builder:/guice/bazel-bin/extensions/servlet/src/com/google/inject/servlet/guice-servlet.jar .
docker stop guice-builder
docker rm guice-builder
