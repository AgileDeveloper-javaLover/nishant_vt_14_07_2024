# nishant_vt_14_07_2024
Sample project for JWT and AOP Implementation


# Implemented many modules in this project

# Module 1: JWT Security
Created jwtservices package that contain all the relavent file to configure jwt security.




# Module 2: AOP (Aspect-Oriented Programming)
Created Validate annotaion to target methods at run time. 
Created ValidateImpl that has implementation for checking the user's permission. 

--------- Explaing the steps ------------
step1: check if token is valid or not
step2: getting permissions from jwt token
step3: matching the user's permission with required permissions for autherization.
step4: if permission match then allow the operation otherwise throw an exception. 


