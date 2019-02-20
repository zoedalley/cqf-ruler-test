# cqf-ruler-test

The purpose of this project is to provide an external test suite for the [cqf-ruler project](https://github.com/DBCG/cqf-ruler). The suite (for FHIR DSTU2 and STU3) includes basic unit tests, measure evaluation tests (coming soon), and CDS tests for the CDC opioid guidelines.

## Usage

 - If your local cqf-ruler instance is running at `http://localhost:8080/cqf-ruler`, simply run `mvn test`
 - Otherwise, specify your url like so: `mvn test -Dtest.url={base url to your cqf-ruler instance}`
 - If you're simply trying to generate the test data for the DSTU2 CDC Opioid Guideline tests and do not have a local instance of the cqf-ruler running, point to the publicly available cqf-ruler: `mvn test -Dtest.url=http://measure.eval.kanvix.com/cqf-ruler` (output in the src/main/resources directory)
 
## TODO

 - Document how to use Junit Categories to specify which tests to run from the command line
 - Include measure evaluation tests (DSTU2 and STU3)
 - Include CDC Opioid Guideline tests (STU3)
 - Include all tests (R4)
 - Expand suites to include Bulk Data Import/Export tests (DSTU2, STU3, and R4)
 - Code cleanup (abstract some dup code, simplify test suites)
