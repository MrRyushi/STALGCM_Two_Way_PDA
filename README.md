# STALGCM_Case_Study
This repository contains the Case Study (Machine Project) for the course Advance Algorithms and Complexities (STALGCM)

# Chosen Model of Computation
Two-Way Finite Accepter / Two-Way: Deterministic Finite Automata

# How to Run the Program
1. Run the Driver Class of the Program <br>
2. Select a text file containing a valid Two-Way Finite Accepter machine definition <br>
3. Input a test string in the text field to test its validity against the machine definition <br>
4. Click the run button <br>
5. Click the step button to simulate the input tape <br>
6. Feedback will be given when the current state is in a accept state or reject state <br>

# Format for the text file containing the machine definition
A,B,C    -- states <br>
0,1      -- input symbols <br>
<        -- left end marker <br>
\>        -- right end marker <br>
A        -- start state <br>
C        -- accept state <br>
B        -- reject state <br>
\-        -- separator <br>
A,a,B,R  -- transition function 1 <br>
B,a,C,R  -- transition function 2 <br>
C,a,A,R  -- transition function 3 <br>
A,b,A,R  -- transition function n <br>

# Sample Machine Definition
A,B,C,D,E,F,G <br>
a,b <br>
< <br>
\> <br>
A <br>
G <br>
F <br>
\- <br>
A,<,A,R <br>
A,a,B,R <br>
B,a,C,R <br>
C,a,A,R <br>
A,b,A,R <br>
B,b,B,R <br>
C,b,C,R <br>
A,>,D,L <br>
B,>,F,L <br>
C,>,F,L <br>
D,<,G,R <br>
E,<,F,R <br>
D,a,D,L <br>
E,a,E,L <br>
D,b,E,L <br>
E,b,D,L <br>
 

