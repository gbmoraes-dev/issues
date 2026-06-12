import { defineConfig } from 'orval'

export default defineConfig({
  issues: {
    input: 'http://localhost:8080/v3/api-docs',
    output: {
      client: 'react-query',
      httpClient: 'axios',
      mode: 'split',
      target: './src/api/endpoints.ts',
      schemas: './src/api/model',
      clean: true,
      override: {
        mutator: {
          path: './src/lib/axios-instance.ts',
          name: 'customInstance',
        },
        query: { useQuery: true, useMutation: true },
      },
    },
  },
})
