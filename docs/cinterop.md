# Kotlin compiler 2.0.0-RC2

macos_arm64
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/macos_arm64/builtIn.def
-o /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-9590572832200510666.klib -target macos_arm64 -verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin -fno-stack-protector -target
arm64-apple-macos11.0 -isysroot
/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.4.sdk -fPIC
-D__ENVIRONMENT_OS_VERSION_MIN_REQUIRED__=__ENVIRONMENT_MAC_OS_X_VERSION_MIN_REQUIRED__ -DTARGET_OS_OSX=1
-Wno-builtin-macro-redefined -include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/2175666148595471239.pch
-include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/17703985476659440289.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-9590572832200510666.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record <
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp11660402281080238285/cstubs.c

macos_x64
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/macos_x64/builtIn.def -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-17709155060262703407.klib -target macos_x64 -verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin -fno-stack-protector -target
x86_64-apple-macos11.0 -isysroot
/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX14.4.sdk -fPIC
-D__ENVIRONMENT_OS_VERSION_MIN_REQUIRED__=__ENVIRONMENT_MAC_OS_X_VERSION_MIN_REQUIRED__ -DTARGET_OS_OSX=1
-Wno-builtin-macro-redefined -include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/13414962302893765952.pch
-include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/16909982767998049753.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-17709155060262703407.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record <
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp13672406210153206439/cstubs.c

linux_x64
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/linux_x64/builtIn.def -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-11715917603924927817.klib -target linux_x64 -verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin -fno-stack-protector
--gcc-toolchain=/Users/Oleg.Yukhnevich/.konan/dependencies/x86_64-unknown-linux-gnu-gcc-8.3.0-glibc-2.19-kernel-4.9-2
-target x86_64-unknown-linux-gnu
--sysroot=/Users/Oleg.Yukhnevich/.konan/dependencies/x86_64-unknown-linux-gnu-gcc-8.3.0-glibc-2.19-kernel-4.9-2/x86_64-unknown-linux-gnu/sysroot
-fPIC -Wno-builtin-macro-redefined -include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/8217420154899769113.pch
-include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/14235972386949311795.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-11715917603924927817.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record < /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp1194316016031301619/cstubs.c

linux_arm64
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/linux_arm64/builtIn.def
-o /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-5826475741575773150.klib -target linux_arm64 -verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin -fno-stack-protector
--gcc-toolchain=/Users/Oleg.Yukhnevich/.konan/dependencies/aarch64-unknown-linux-gnu-gcc-8.3.0-glibc-2.25-kernel-4.9-2
-target aarch64-unknown-linux-gnu
--sysroot=/Users/Oleg.Yukhnevich/.konan/dependencies/aarch64-unknown-linux-gnu-gcc-8.3.0-glibc-2.25-kernel-4.9-2/aarch64-unknown-linux-gnu/sysroot
-fPIC -Wno-builtin-macro-redefined -include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/5286360109088170897.pch
-include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/587104147976698553.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-5826475741575773150.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record <
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp16780867980706924909/cstubs.c

mingw_x64
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/mingw_x64/builtIn.def -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-2363488710512434041.klib -target mingw_x64 -verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin -fno-stack-protector
-target x86_64-pc-windows-gnu --sysroot=/Users/Oleg.Yukhnevich/.konan/dependencies/msys2-mingw-w64-x86_64-2
-Wno-builtin-macro-redefined -include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/4125158492428796140.pch
-include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/4576751788950708628.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-2363488710512434041.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record < /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp8023808775468743296/cstubs.c

ios_x64
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/ios_x64/builtIn.def -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-7255509632712743935.klib -target ios_x64 -verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin -fno-stack-protector -target
x86_64-apple-ios12.0-simulator -isysroot
/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator17.4.sdk
-fPIC -D__ENVIRONMENT_OS_VERSION_MIN_REQUIRED__=__ENVIRONMENT_IPHONE_OS_VERSION_MIN_REQUIRED__
-Wno-builtin-macro-redefined -include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/7128664737070140373.pch
-include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/1574698008515481882.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-7255509632712743935.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record <
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp11750405352120785895/cstubs.c

ios_arm64
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/ios_arm64/builtIn.def -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-4318285140887535509.klib -target ios_arm64 -verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin -fno-stack-protector -target
arm64-apple-ios12.0 -isysroot
/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS17.4.sdk -fPIC
-D__ENVIRONMENT_OS_VERSION_MIN_REQUIRED__=__ENVIRONMENT_IPHONE_OS_VERSION_MIN_REQUIRED__ -DTARGET_OS_IOS=1
-DTARGET_OS_EMBEDDED=1 -DTARGET_OS_IPHONE=1 -Wno-builtin-macro-redefined -include-pch
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/6054268659036279469.pch -include-pch
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/7837144489011384254.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-4318285140887535509.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record < /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp8093091406762126367/cstubs.c

ios_simulator_arm64
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/ios_simulator_arm64/builtIn.def
-o /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-14774087212271725798.klib -target ios_simulator_arm64
-verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin -fno-stack-protector -target
arm64-apple-ios14.0-simulator -isysroot
/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator17.4.sdk
-fPIC -D__ENVIRONMENT_OS_VERSION_MIN_REQUIRED__=__ENVIRONMENT_IPHONE_OS_VERSION_MIN_REQUIRED__
-Wno-builtin-macro-redefined -include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/16657860970514456014.pch
-include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/10417010533337862883.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-14774087212271725798.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record <
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp13039595002989829696/cstubs.c

tvos_arm64
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/tvos_arm64/builtIn.def -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-8077637151459603393.klib -target tvos_arm64 -verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin -fno-stack-protector -target
arm64-apple-tvos12.0 -isysroot
/Applications/Xcode.app/Contents/Developer/Platforms/AppleTVOS.platform/Developer/SDKs/AppleTVOS17.4.sdk -fPIC
-D__ENVIRONMENT_OS_VERSION_MIN_REQUIRED__=__ENVIRONMENT_TV_OS_VERSION_MIN_REQUIRED__ -DTARGET_OS_TV=1
-DTARGET_OS_EMBEDDED=1 -DTARGET_OS_IPHONE=1 -Wno-builtin-macro-redefined -include-pch
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/8186111825945259502.pch -include-pch
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/5849886718239459355.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-8077637151459603393.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record < /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp9795821994222110912/cstubs.c

tvos_x64
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/tvos_x64/builtIn.def -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-17096984459513295957.klib -target tvos_x64 -verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin -fno-stack-protector -target
x86_64-apple-tvos12.0-simulator -isysroot
/Applications/Xcode.app/Contents/Developer/Platforms/AppleTVSimulator.platform/Developer/SDKs/AppleTVSimulator17.4.sdk
-fPIC -D__ENVIRONMENT_OS_VERSION_MIN_REQUIRED__=__ENVIRONMENT_TV_OS_VERSION_MIN_REQUIRED__ -Wno-builtin-macro-redefined
-include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/15445396353693721937.pch -include-pch
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/11239998446356475980.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-17096984459513295957.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record < /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp2031929239436479746/cstubs.c

tvos_simulator_arm64
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/tvos_simulator_arm64/builtIn.def
-o /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-14814854166143535167.klib -target tvos_simulator_arm64
-verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin -fno-stack-protector -target
arm64-apple-tvos14.0-simulator -isysroot
/Applications/Xcode.app/Contents/Developer/Platforms/AppleTVSimulator.platform/Developer/SDKs/AppleTVSimulator17.4.sdk
-fPIC -D__ENVIRONMENT_OS_VERSION_MIN_REQUIRED__=__ENVIRONMENT_TV_OS_VERSION_MIN_REQUIRED__ -Wno-builtin-macro-redefined
-include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/13282922719642069889.pch -include-pch
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/4088945119502676497.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-14814854166143535167.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record <
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp18180663854030598460/cstubs.c

watchos_arm32
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/watchos_arm32/builtIn.def
-o /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-11489622924549056185.klib -target watchos_arm32 -verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin -fno-stack-protector -target
armv7k-apple-watchos5.0 -isysroot
/Applications/Xcode.app/Contents/Developer/Platforms/WatchOS.platform/Developer/SDKs/WatchOS10.4.sdk -fPIC
-D__ENVIRONMENT_OS_VERSION_MIN_REQUIRED__=__ENVIRONMENT_WATCH_OS_VERSION_MIN_REQUIRED__ -DTARGET_OS_EMBEDDED=1
-DTARGET_OS_IPHONE=1 -DTARGET_OS_WATCH=1 -marm -Wno-builtin-macro-redefined -include-pch
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/3829581181973401054.pch -include-pch
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/17048282053837661487.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-11489622924549056185.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record <
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp12573442075748141557/cstubs.c

watchos_arm64
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/watchos_arm64/builtIn.def
-o /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-8957679533248021907.klib -target watchos_arm64 -verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin -fno-stack-protector -target
arm64_32-apple-watchos5.0 -isysroot
/Applications/Xcode.app/Contents/Developer/Platforms/WatchOS.platform/Developer/SDKs/WatchOS10.4.sdk -fPIC
-D__ENVIRONMENT_OS_VERSION_MIN_REQUIRED__=__ENVIRONMENT_WATCH_OS_VERSION_MIN_REQUIRED__ -DTARGET_OS_EMBEDDED=1
-DTARGET_OS_IPHONE=1 -DTARGET_OS_WATCH=1 -Wno-builtin-macro-redefined -include-pch
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/4054033995248808610.pch -include-pch
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/11740164924191421477.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-8957679533248021907.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record < /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp1452196426823599280/cstubs.c

watchos_x64
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/watchos_x64/builtIn.def
-o /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-15002122686531371219.klib -target watchos_x64 -verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin -fno-stack-protector -target
x86_64-apple-watchos7.0-simulator -isysroot
/Applications/Xcode.app/Contents/Developer/Platforms/WatchSimulator.platform/Developer/SDKs/WatchSimulator10.4.sdk -fPIC
-D__ENVIRONMENT_OS_VERSION_MIN_REQUIRED__=__ENVIRONMENT_WATCH_OS_VERSION_MIN_REQUIRED__ -Wno-builtin-macro-redefined
-include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/13758966643436658022.pch -include-pch
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/7889579117833037328.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-15002122686531371219.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record <
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp15049482977807107009/cstubs.c

watchos_device_arm64
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/watchos_device_arm64/builtIn.def
-o /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-4555479085429530804.klib -target watchos_device_arm64
-verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin -fno-stack-protector -target
arm64-apple-watchos9.0 -isysroot
/Applications/Xcode.app/Contents/Developer/Platforms/WatchOS.platform/Developer/SDKs/WatchOS10.4.sdk -fPIC
-D__ENVIRONMENT_OS_VERSION_MIN_REQUIRED__=__ENVIRONMENT_WATCH_OS_VERSION_MIN_REQUIRED__ -DTARGET_OS_EMBEDDED=1
-DTARGET_OS_IPHONE=1 -DTARGET_OS_WATCH=1 -Wno-builtin-macro-redefined -include-pch
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/15384577696873792523.pch -include-pch
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/1514847805186137941.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-4555479085429530804.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record <
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp11605201619118828784/cstubs.c

watchos_simulator_arm64
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/watchos_simulator_arm64/builtIn.def
-o /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-2247028038684603341.klib -target watchos_simulator_arm64
-verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin -fno-stack-protector -target
arm64-apple-watchos7.0-simulator -isysroot
/Applications/Xcode.app/Contents/Developer/Platforms/WatchSimulator.platform/Developer/SDKs/WatchSimulator10.4.sdk -fPIC
-D__ENVIRONMENT_OS_VERSION_MIN_REQUIRED__=__ENVIRONMENT_WATCH_OS_VERSION_MIN_REQUIRED__ -Wno-builtin-macro-redefined
-include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/9542267021729187578.pch -include-pch
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/15279766811267495787.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-2247028038684603341.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record < /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp276925977726624649/cstubs.c

android_x64
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/android_x64/builtIn.def
-o /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-8493274161497797437.klib -target android_x64 -verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Users/Oleg.Yukhnevich/.konan/dependencies/target-toolchain-2-osx-android_ndk/bin -fno-stack-protector -target
x86_64-unknown-linux-android -fPIC -D__ANDROID_API__=21
--sysroot=/Users/Oleg.Yukhnevich/.konan/dependencies/target-sysroot-1-android_ndk/android-21/arch-x86_64
-I/Users/Oleg.Yukhnevich/.konan/dependencies/target-toolchain-2-osx-android_ndk/sysroot/usr/include/c++/v1
-I/Users/Oleg.Yukhnevich/.konan/dependencies/target-toolchain-2-osx-android_ndk/sysroot/usr/include
-I/Users/Oleg.Yukhnevich/.konan/dependencies/target-toolchain-2-osx-android_ndk/sysroot/usr/include/x86_64-linux-android
-Wno-builtin-macro-redefined -include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/15964982954690831803.pch
-include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/10860128119524749559.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-8493274161497797437.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record < /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp3460419949763131623/cstubs.c

android_x86
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/android_x86/builtIn.def
-o /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-13473714656737619033.klib -target android_x86 -verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Users/Oleg.Yukhnevich/.konan/dependencies/target-toolchain-2-osx-android_ndk/bin -fno-stack-protector -target
i686-unknown-linux-android -fPIC -D__ANDROID_API__=21
--sysroot=/Users/Oleg.Yukhnevich/.konan/dependencies/target-sysroot-1-android_ndk/android-21/arch-x86
-I/Users/Oleg.Yukhnevich/.konan/dependencies/target-toolchain-2-osx-android_ndk/sysroot/usr/include/c++/v1
-I/Users/Oleg.Yukhnevich/.konan/dependencies/target-toolchain-2-osx-android_ndk/sysroot/usr/include
-I/Users/Oleg.Yukhnevich/.konan/dependencies/target-toolchain-2-osx-android_ndk/sysroot/usr/include/i686-linux-android
-Wno-builtin-macro-redefined -include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/17062153780210766257.pch
-include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/10720768136282605907.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-13473714656737619033.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record <
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp17405681841758721757/cstubs.c

android_arm32
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/android_arm32/builtIn.def
-o /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-9367786203678865305.klib -target android_arm32 -verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Users/Oleg.Yukhnevich/.konan/dependencies/target-toolchain-2-osx-android_ndk/bin -fno-stack-protector -target
arm-unknown-linux-androideabi -fPIC -D__ANDROID_API__=21
--sysroot=/Users/Oleg.Yukhnevich/.konan/dependencies/target-sysroot-1-android_ndk/android-21/arch-arm
-I/Users/Oleg.Yukhnevich/.konan/dependencies/target-toolchain-2-osx-android_ndk/sysroot/usr/include/c++/v1
-I/Users/Oleg.Yukhnevich/.konan/dependencies/target-toolchain-2-osx-android_ndk/sysroot/usr/include
-I/Users/Oleg.Yukhnevich/.konan/dependencies/target-toolchain-2-osx-android_ndk/sysroot/usr/include/arm-linux-androideabi
-Wno-builtin-macro-redefined -include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/5701049145453496929.pch
-include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/6499835180384129771.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-9367786203678865305.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record <
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp13072302317715142349/cstubs.c

android_arm64
CLI COMMAND: /Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/bin/cinterop -def
/Users/Oleg.Yukhnevich/.konan/kotlin-native-prebuilt-macos-aarch64-2.0.0-RC2/konan/platformDef/android_arm64/builtIn.def
-o /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-14625235990381673237.klib -target android_arm64 -verbose
-----
CINTEROP COMMAND: /Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/bin/clang -O2
-fexceptions -isystem
/Users/Oleg.Yukhnevich/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/lib/clang/11.1.0/include
-B/Users/Oleg.Yukhnevich/.konan/dependencies/target-toolchain-2-osx-android_ndk/bin -fno-stack-protector -target
aarch64-unknown-linux-android -fPIC -D__ANDROID_API__=21
--sysroot=/Users/Oleg.Yukhnevich/.konan/dependencies/target-sysroot-1-android_ndk/android-21/arch-arm64
-I/Users/Oleg.Yukhnevich/.konan/dependencies/target-toolchain-2-osx-android_ndk/sysroot/usr/include/c++/v1
-I/Users/Oleg.Yukhnevich/.konan/dependencies/target-toolchain-2-osx-android_ndk/sysroot/usr/include
-I/Users/Oleg.Yukhnevich/.konan/dependencies/target-toolchain-2-osx-android_ndk/sysroot/usr/include/aarch64-linux-android
-Wno-builtin-macro-redefined -include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/136237595698309001.pch
-include-pch /var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/16831642691285157301.pch -emit-llvm -x c -c - -o
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/cinterop-14625235990381673237.klib-build/natives/cstubs.bc -Xclang
-detailed-preprocessing-record <
/var/folders/t0/r4zzg8js2gnchztmlh57dyn80000gp/T/konan_temp13187415056608051133/cstubs.c

