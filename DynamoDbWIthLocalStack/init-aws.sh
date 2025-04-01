#!/bin/bash
# init-aws.sh: Create DynamoDB table and GSI in LocalStack on startup

set -e #stop on errors

TABLE_NAME="Products"
REGION="us-west-2"

echo "Checking if table '$TABLE_NAME' exists..."

# Check if table exists
if awslocal dynamodb describe-table --table-name "$TABLE_NAME" > /dev/null 2>&1; then
  echo "Deleting table '$TABLE_NAME'..."
  awslocal dynamodb delete-table --table-name "$TABLE_NAME"
  echo "Waiting for table deletion to complete..."
  sleep 2  # Optional, for LocalStack stability
else
  echo "Table '$TABLE_NAME' does not exist, skipping delete."
fi

echo "Creating table '$TABLE_NAME'..."
awslocal dynamodb create-table \
  --table-name Products \
  --attribute-definitions \
    AttributeName=id,AttributeType=S \
    AttributeName=category,AttributeType=S \
  --key-schema AttributeName=id,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST \
  --global-secondary-indexes '[
      {
          "IndexName": "CategoryIndex",
          "KeySchema": [ { "AttributeName": "category", "KeyType": "HASH" } ],
          "Projection": { "ProjectionType": "ALL" },
          "ProvisionedThroughput": { "ReadCapacityUnits": 5, "WriteCapacityUnits": 5 }
      }
  ]'
echo "Table '$TABLE_NAME' reset complete!"



# command to check the tables created on terminal
# aws dynamodb list-tables --endpoint-url http://localhost:4566 --region us-west-2 | Out-Host