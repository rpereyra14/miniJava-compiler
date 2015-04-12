  0         JUMP         L12
  1  L10:   PUSH         1
  2         LOADL        0
  3         STORE        3[LB]
  4         LOAD         3[LB]
  5         LOADL        0
  6         CALL         eq      
  7         JUMPIF (0)   L11
  8         LOADL        6
  9         CALL         putint  
 10  L11:   RETURN (0)   0
 11  L12:   LOADL        -1
 12         LOADL        1
 13         LOADA        1[CB]
 14         LOADL        -1
 15         CALL         L10
 16         HALT   (0)   
