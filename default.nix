with import <nixpkgs> {};
stdenv.mkDerivation rec {
  name = "env";
  env = buildEnv { name = name; paths = buildInputs; };
  buildInputs = [
    cmake
    extra-cmake-modules
    # conan # Need to make it conditional for Linux-only
    clojure
    go_1_10
    maven
    nodejs-10_x
    openjdk
    qt511.full
    watchman
    unzip
    wget # 1.19.4
    yarn
  ];
}