# adapted from https://github.com/glassfish/docker

FROM rugwacc23/glassfish_base

# Set environment variables and default password for user 'admin'
ENV GLASSFISH_PKG=glassfish-4.1.1.zip \
				  GLASSFISH_URL=http://download.oracle.com/glassfish/4.1.1/release/glassfish-4.1.1.zip \
				  GLASSFISH_HOME=/glassfish4 \
				  PATH=$PATH:/glassfish4/bin \
				  PASSWORD=glassfish
COPY . /tmp

# configure glassfish and deploy .war
RUN echo "--- Setup the password file ---" && \
  echo "AS_ADMIN_PASSWORD=" > /tmp/glassfishpwd && \
  echo "AS_ADMIN_NEWPASSWORD=${PASSWORD}" >> /tmp/glassfishpwd  && \
  echo "--- Enable DAS, change admin password, and secure admin access ---" && \
  asadmin --user=admin --passwordfile=/tmp/glassfishpwd change-admin-password --domain_name domain1 && \
  asadmin start-domain && \
  echo "AS_ADMIN_PASSWORD=${PASSWORD}" > /tmp/glassfishpwd && \
  asadmin --user=admin --passwordfile=/tmp/glassfishpwd enable-secure-admin && \
  asadmin --user=admin --passwordfile=/tmp/glassfishpwd deploy /tmp/UberForBikesWACC.war && \
  asadmin --user=admin stop-domain
					  #rm /tmp/glassfishpwd && \

# Ports being exposed
EXPOSE 8080

# Start asadmin console and the domain
CMD ["asadmin", "start-domain", "-v"]
