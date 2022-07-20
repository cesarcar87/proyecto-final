#### Stage 1: Build the react application
FROM node:12.4.0-alpine as build

# Configure the main working directory inside the docker image. 
# This is the base directory used in any further RUN, COPY, and ENTRYPOINT 
# commands.
WORKDIR /app

COPY node/package.json node/package-lock.json ./

# Copy the main application
COPY node/* ./

RUN npm install

# Arguments
ARG REACT_APP_API_BASE_URL
ENV REACT_APP_API_BASE_URL=${REACT_APP_API_BASE_URL}

EXPOSE 3000

CMD ["npm", "start"]
