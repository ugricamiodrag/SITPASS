# Use the official Elasticsearch image as a base
FROM docker.elastic.co/elasticsearch/elasticsearch:8.15.0

# Install ICU plugin (with --batch to auto-confirm)
RUN elasticsearch-plugin install --batch analysis-icu
