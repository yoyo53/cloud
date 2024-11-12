FROM node:19-alpine AS base

WORKDIR /app

ENV NODE_ENV="production"

COPY . .

FROM base AS build

RUN npm install

FROM base AS production

COPY --from=build /app/node_modules /app/node_modules

CMD [ "npm", "run", "start" ]