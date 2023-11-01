#!/bin/bash

target_dir="server/target"
client_dir="client/target"
temp_dir="tmp"

mvn clean install

mkdir -p "$temp_dir"

cp "$target_dir/tpe2-g6-server-2023.2Q-bin.tar.gz" "$temp_dir/"
cp "$client_dir/tpe2-g6-client-2023.2Q-bin.tar.gz" "$temp_dir/"
cd "$temp_dir"

# Server
tar -xzf "tpe2-g6-server-2023.2Q-bin.tar.gz"
chmod +x tpe2-g6-server-2023.2Q/run-server
sed -i -e 's/\r$//' tpe2-g6-server-2023.2Q/run-server
rm "tpe2-g6-server-2023.2Q-bin.tar.gz"

# Client
tar -xzf "tpe2-g6-client-2023.2Q-bin.tar.gz"
chmod +x tpe2-g6-client-2023.2Q/query*
sed -i -e 's/\r$//' tpe2-g6-client-2023.2Q/query*
rm "tpe2-g6-client-2023.2Q-bin.tar.gz"
