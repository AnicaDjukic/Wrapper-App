import json
import sys

class Realizacija:
    def __init__(self, godina, semestar, studijskiProgramPredmeti):
        self.godina = godina
        self.semestar = semestar
        self.studijskiProgramPredmeti = studijskiProgramPredmeti

# Read the JSON file path from command-line argument
json_file_path = sys.argv[1]

# Read the JSON data from the file with specified encoding
with open(json_file_path, encoding='utf-8') as file:
    json_input = file.read()

# Parse the JSON into a Python object
input_data = json.loads(json_input)

# Map the attribute names in input_data to the expected attribute names in Realizacija
mapped_input_data = {
    'godina': input_data.get('godina'),
    'semestar': input_data.get('semestar'),
    'studijskiProgramPredmeti': input_data.get('studijskiProgramPredmeti')
}

# Deserialize the JSON into Realizacija object
realizacija = Realizacija(**mapped_input_data)

# Perform any necessary operations on realizacija object
# ...

# Serialize the updated Realizacija object to JSON
json_updated_realizacija = json.dumps(realizacija.__dict__)

# Print the JSON representation
print(json_updated_realizacija)

# Flush the output to ensure it's immediately available
sys.stdout.flush()
