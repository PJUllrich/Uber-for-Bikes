# Downloads glassfish but does not set it up, can be uses as a base image for our real server

# Ideally, we don't have to do this again:
# sudo docker build -t rugwacc23/glassfish_base .
# get image id:
# sudo docker images
# sudo docker tag <image id> rugwacc23/glassfish_base
# sudo docker login
# sudo docker push rugwacc23/glassfish_base

FROM glassfish/openjdk

# Set environment variables and default password for user 'admin'
ENV GLASSFISH_PKG=glassfish-4.1.1.zip \
				  GLASSFISH_URL=http://download.oracle.com/glassfish/4.1.1/release/glassfish-4.1.1.zip \
				  GLASSFISH_HOME=/glassfish4 \
				  PATH=$PATH:/glassfish4/bin \
				  PASSWORD=glassfish

# Install packages, download and extract GlassFish
# Setup password file
# Enable DAS
RUN apk add --update wget unzip && \
  wget --no-check-certificate $GLASSFISH_URL && \
  unzip -o $GLASSFISH_PKG && \
  rm -f $GLASSFISH_PKG && \
  apk del wget unzip