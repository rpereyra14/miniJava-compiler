  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADA        3[SB]
  3         LOADL        1
  4         CALL         newobj  
  5         STORE        3[LB]
  6         LOADL        57
  7         LOAD         3[LB]
  8         CALL         L12
  9         STORE        3[LB]
 10         LOAD         3[LB]
 11         LOADL        0
 12         CALL         fieldref
 13         CALL         putint  
 14         RETURN (0)   0
 15  L11:   LOADL        -1
 16         LOADL        1
 17         LOADA        1[CB]
 18         JUMP         L14
 19  L12:   PUSH         1
 20         LOADA        3[SB]
 21         LOADL        1
 22         CALL         newobj  
 23         STORE        4[LB]
 24         LOAD         4[LB]
 25         LOADL        0
 26         LOAD         -1[LB]
 27         CALL         fieldupd
 28         PUSH         1
 29         LOAD         4[LB]
 30         CALL         L13
 31         STORE        5[LB]
 32         LOAD         5[LB]
 33         RETURN (1)   1
 34  L13:   LOADA        0[OB]
 35         RETURN (1)   0
 36  L14:   LOADL        -1
 37         LOADL        2
 38         LOADA        19[CB]
 39         LOADA        34[CB]
 40         LOADL        -1
 41         CALL         L10
 42         HALT   (0)   
