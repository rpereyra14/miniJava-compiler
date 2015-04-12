  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADA        3[SB]
  3         LOADL        1
  4         CALL         newobj  
  5         STORE        3[LB]
  6         LOAD         3[LB]
  7         LOADL        0
  8         LOADL        1
  9         CALL         neg     
 10         CALL         fieldupd
 11         LOADL        17
 12         LOAD         3[LB]
 13         CALL         L12
 14         STORE        3[LB]
 15         LOAD         3[LB]
 16         LOADL        0
 17         CALL         fieldref
 18         CALL         putint  
 19         RETURN (0)   0
 20  L11:   LOADL        -1
 21         LOADL        1
 22         LOADA        1[CB]
 23         JUMP         L14
 24  L12:   PUSH         1
 25         LOADA        3[SB]
 26         LOADL        1
 27         CALL         newobj  
 28         STORE        4[LB]
 29         LOAD         4[LB]
 30         LOADL        0
 31         LOAD         -1[LB]
 32         CALL         fieldupd
 33         PUSH         1
 34         LOAD         4[LB]
 35         CALL         L13
 36         STORE        5[LB]
 37         LOAD         5[LB]
 38         RETURN (1)   1
 39  L13:   LOADA        0[OB]
 40         RETURN (1)   0
 41  L14:   LOADL        -1
 42         LOADL        2
 43         LOADA        24[CB]
 44         LOADA        39[CB]
 45         LOADL        -1
 46         CALL         L10
 47         HALT   (0)   
