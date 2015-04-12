  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADA        3[SB]
  3         LOADL        1
  4         CALL         newobj  
  5         STORE        3[LB]
  6         LOAD         3[LB]
  7         LOADL        0
  8         LOADL        3
  9         CALL         fieldupd
 10         RETURN (0)   0
 11  L11:   LOADL        -1
 12         LOADL        1
 13         LOADA        1[CB]
 14         JUMP         L12
 15  L12:   LOADL        -1
 16         LOADL        0
 17         LOADL        -1
 18         CALL         L10
 19         HALT   (0)   
