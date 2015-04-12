  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADA        3[SB]
  3         LOADL        1
  4         CALL         newobj  
  5         STORE        3[LB]
  6         LOAD         3[LB]
  7         LOADL        0
  8         LOADL        56
  9         CALL         fieldupd
 10         LOADL        999
 11         LOAD         3[LB]
 12         CALL         L12
 13         LOAD         3[LB]
 14         CALL         L13
 15         CALL         putint  
 16         RETURN (0)   0
 17  L11:   LOADL        -1
 18         LOADL        1
 19         LOADA        1[CB]
 20         JUMP         L14
 21  L12:   PUSH         1
 22         LOADL        0
 23         STORE        4[LB]
 24         LOAD         -1[LB]
 25         STORE        4[LB]
 26         LOADA        0[OB]
 27         LOADL        0
 28         LOAD         4[LB]
 29         CALL         fieldupd
 30         RETURN (0)   1
 31  L13:   LOADA        0[OB]
 32         LOADL        0
 33         CALL         fieldref
 34         RETURN (1)   0
 35  L14:   LOADL        -1
 36         LOADL        2
 37         LOADA        21[CB]
 38         LOADA        31[CB]
 39         LOADL        -1
 40         CALL         L10
 41         HALT   (0)   
