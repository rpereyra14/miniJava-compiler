  0         JUMP         L11
  1  L10:   RETURN (0)   0
  2         PUSH         1
  3         LOAD         -1[LB]
  4         STORE        3[LB]
  5         PUSH         1
  6         LOADL        3
  7         STORE        4[LB]
  8         RETURN (0)   1
  9  L11:   LOADL        -1
 10         LOADL        2
 11         LOADA        1[CB]
 12         LOADA        2[CB]
 13         LOADL        -1
 14         CALL         L10
 15         HALT   (0)   
