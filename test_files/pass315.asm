  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADL        3
  3         STORE        3[LB]
  4         PUSH         1
  5         LOADL        4
  6         STORE        4[LB]
  7         POP          1
  8         PUSH         1
  9         LOADL        10
 10         STORE        4[LB]
 11         PUSH         1
 12         LOADL        5
 13         STORE        5[LB]
 14         POP          2
 15         RETURN (0)   0
 16  L11:   LOADL        -1
 17         LOADL        1
 18         LOADA        1[CB]
 19         LOADL        -1
 20         CALL         L10
 21         HALT   (0)   
