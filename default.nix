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
    leiningen
    maven
    nodejs-10_x
    openjdk
    # qt511.full # does not install on macOS due to incompatible dependency `bluez`
    qt5.qtbase
    qt5.qtdeclarative
    qt5.qtgraphicaleffects
    qt5.qtimageformats
    qt5.qtlocation
    qt5.qtquickcontrols
    qt5.qtquickcontrols2
    qt5.qtsensors
    qt5.qtserialport
    qt5.qtsvg
    qt5.qttools
    qt5.qtwebchannel
    qt5.qttranslations
    watchman
    unzip
    wget # 1.19.4
    yarn
  ];
}