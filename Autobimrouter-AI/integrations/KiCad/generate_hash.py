import hashlib
import os

def generate_hash_and_size(file_path):
    # Calculate the SHA-256 hash of the file
    sha256_hash = hashlib.sha256()
    with open(file_path, "rb") as f:
        for byte_block in iter(lambda: f.read(4096), b""):
            sha256_hash.update(byte_block)
    
    # Get the file size
    file_size = os.path.getsize(file_path)
    
    return sha256_hash.hexdigest(), file_size

file_path = 'kicad-freerouting-2.3.4.zip'
file_hash, file_size = generate_hash_and_size(file_path)

print(f"Hash: {file_hash}")
print(f"Size: {file_size}")
