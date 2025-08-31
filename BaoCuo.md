chen@U-29M93MPH-2253 AliPathMaker-Web-Server_Side-main % bash deploy.sh      
[+] Building 436.5s (9/16)                                                                                        docker:desktop-linux
 => [internal] load build definition from Dockerfile                                                                              0.0s
 => => transferring dockerfile: 1.39kB                                                                                            0.0s
 => [internal] load metadata for docker.io/library/python:3.10-slim                                                               0.5s
 => [internal] load .dockerignore                                                                                                 0.0s
 => => transferring context: 2B                                                                                                   0.0s
 => [ 1/12] FROM docker.io/library/python:3.10-slim@sha256:420fbb0e468d3eaf0f7e93ea6f7a48792cbcadc39d43ac95b96bee2afe4367da       0.0s
 => => resolve docker.io/library/python:3.10-slim@sha256:420fbb0e468d3eaf0f7e93ea6f7a48792cbcadc39d43ac95b96bee2afe4367da         0.0s
 => [internal] load build context                                                                                                 0.0s
 => => transferring context: 187.28kB                                                                                             0.0s
 => [ 2/12] WORKDIR /app                                                                                                          0.0s
 => [ 3/12] COPY requirements.txt .                                                                                               0.0s
 => [ 4/12] RUN pip install --no-cache-dir -r requirements.txt                                                                  136.9s
 => ERROR [ 5/12] RUN apt-get update &&     apt-get install -y --no-install-recommends     build-essential     git     wget     191.0s 
------                                                                                                                                 
 > [ 5/12] RUN apt-get update &&     apt-get install -y --no-install-recommends     build-essential     git     wget     pkg-config     libexpat1-dev     libpng-dev     libgd-dev     libfreetype6-dev     p7zip-full     bash     fonts-wqy-zenhei:                         
8.628 Hit:1 http://deb.debian.org/debian trixie InRelease                                                                              
8.953 Get:2 http://deb.debian.org/debian trixie-updates InRelease [47.1 kB]                                                            
11.12 Get:3 http://deb.debian.org/debian-security trixie-security InRelease [43.4 kB]                                                  
21.39 Get:4 http://deb.debian.org/debian trixie/main arm64 Packages [9604 kB]
183.9 Ign:4 http://deb.debian.org/debian trixie/main arm64 Packages
183.9 Ign:5 http://deb.debian.org/debian trixie-updates/main arm64 Packages
183.9 Ign:6 http://deb.debian.org/debian-security trixie-security/main arm64 Packages
184.9 Ign:6 http://deb.debian.org/debian-security trixie-security/main arm64 Packages
184.9 Ign:5 http://deb.debian.org/debian trixie-updates/main arm64 Packages
184.9 Ign:4 http://deb.debian.org/debian trixie/main arm64 Packages
187.0 Ign:4 http://deb.debian.org/debian trixie/main arm64 Packages
187.0 Ign:5 http://deb.debian.org/debian trixie-updates/main arm64 Packages
187.0 Ign:6 http://deb.debian.org/debian-security trixie-security/main arm64 Packages
191.0 Ign:6 http://deb.debian.org/debian-security trixie-security/main arm64 Packages
191.0 Ign:5 http://deb.debian.org/debian trixie-updates/main arm64 Packages
191.0 Ign:4 http://deb.debian.org/debian trixie/main arm64 Packages
191.0 Ign:6 http://deb.debian.org/debian-security trixie-security/main arm64 Packages
191.0 Ign:5 http://deb.debian.org/debian trixie-updates/main arm64 Packages
191.0 Ign:4 http://deb.debian.org/debian trixie/main arm64 Packages
191.0 Ign:6 http://deb.debian.org/debian-security trixie-security/main arm64 Packages
191.0 Ign:5 http://deb.debian.org/debian trixie-updates/main arm64 Packages
191.0 Ign:4 http://deb.debian.org/debian trixie/main arm64 Packages
191.0 Err:6 http://deb.debian.org/debian-security trixie-security/main arm64 Packages
191.0   Could not resolve 'deb.debian.org' [IP: 146.75.46.132 80]
191.0 Err:5 http://deb.debian.org/debian trixie-updates/main arm64 Packages
191.0   Could not resolve 'deb.debian.org' [IP: 146.75.46.132 80]
191.0 Err:4 http://deb.debian.org/debian trixie/main arm64 Packages
191.0   Connection timed out [IP: 146.75.46.132 80]
191.0   Could not resolve 'deb.debian.org' [IP: 146.75.46.132 80]
191.0 Ign:4 http://deb.debian.org/debian trixie/main arm64 Packages
191.0 Err:4 http://deb.debian.org/debian trixie/main arm64 Packages
191.0   Connection timed out [IP: 146.75.46.132 80]
191.0   Could not resolve 'deb.debian.org' [IP: 146.75.46.132 80]
191.0 Fetched 90.5 kB in 3min 11s (474 B/s)
191.0 Reading package lists...
191.0 W: Failed to fetch http://deb.debian.org/debian/dists/trixie/main/binary-arm64/Packages  Could not resolve 'deb.debian.org' [IP: 146.75.46.132 80]
191.0 W: Failed to fetch http://deb.debian.org/debian/dists/trixie-updates/main/binary-arm64/Packages  Could not resolve 'deb.debian.org' [IP: 146.75.46.132 80]
191.0 W: Failed to fetch http://deb.debian.org/debian-security/dists/trixie-security/main/binary-arm64/Packages  Could not resolve 'deb.debian.org' [IP: 146.75.46.132 80]
191.0 W: Some index files failed to download. They have been ignored, or old ones used instead.
191.0 Reading package lists...
191.0 Building dependency tree...
191.0 Reading state information...
191.0 E: Unable to locate package build-essential
191.0 E: Unable to locate package git
191.0 E: Unable to locate package wget
191.0 E: Unable to locate package pkg-config
191.0 E: Unable to locate package libexpat1-dev
191.0 E: Unable to locate package libpng-dev
191.0 E: Unable to locate package libgd-dev
191.0 E: Unable to locate package libfreetype6-dev
191.0 E: Unable to locate package p7zip-full
191.0 E: Unable to locate package fonts-wqy-zenhei
------
Dockerfile:12
--------------------
  11 |     # 安装系统依赖
  12 | >>> RUN apt-get update && \
  13 | >>>     apt-get install -y --no-install-recommends \
  14 | >>>     build-essential \
  15 | >>>     git \
  16 | >>>     wget \
  17 | >>>     pkg-config \
  18 | >>>     libexpat1-dev \
  19 | >>>     libpng-dev \
  20 | >>>     libgd-dev \
  21 | >>>     libfreetype6-dev \
  22 | >>>     p7zip-full \
  23 | >>>     bash \
  24 | >>>     fonts-wqy-zenhei
  25 |     
--------------------
ERROR: failed to build: failed to solve: process "/bin/sh -c apt-get update &&     apt-get install -y --no-install-recommends     build-essential     git     wget     pkg-config     libexpat1-dev     libpng-dev     libgd-dev     libfreetype6-dev     p7zip-full     bash     fonts-wqy-zenhei" did not complete successfully: exit code: 100

View build details: docker-desktop://dashboard/build/desktop-linux/desktop-linux/5mz3t0jzhppu7pekhclcm44pg
alipathmarker-backend
ade2666f55a019cf8aa0f2d2bf2eacabc82d3293d76bdf8e5c9007a42b25121a
