  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADA        3[SB]
  3         LOADL        2
  4         CALL         newobj  
  5         STORE        3[LB]
  6         PUSH         1
  7         LOADA        0[SB]
  8         LOADL        1
  9         CALL         newobj  
 10         STORE        4[LB]
 11         LOAD         4[LB]
 12         LOADL        0
 13         LOAD         3[LB]
 14         CALL         fieldupd
 15         LOAD         3[LB]
 16         LOADL        0
 17         LOAD         4[LB]
 18         CALL         fieldupd
 19         PUSH         1
 20         LOAD         4[LB]
 21         LOADL        0
 22         CALL         fieldref
 23         LOADL        0
 24         CALL         fieldref
 25         LOADL        0
 26         CALL         fieldref
 27         LOADL        1
 28         CALL         fieldref
 29         STORE        5[LB]
 30         RETURN (0)   0
 31  L11:   LOADL        -1
 32         LOADL        1
 33         LOADA        1[CB]
 34         JUMP         L12
 35  L12:   LOADL        -1
 36         LOADL        0
 37         LOADL        -1
 38         CALL         L10
 39         HALT   (0)   
