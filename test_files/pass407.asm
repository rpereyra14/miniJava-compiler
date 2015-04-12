  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADA        3[SB]
  3         LOADL        2
  4         CALL         newobj  
  5         STORE        3[LB]
  6         LOAD         3[LB]
  7         LOADL        1
  8         LOADA        5[SB]
  9         LOADL        2
 10         CALL         newobj  
 11         CALL         fieldupd
 12         LOAD         3[LB]
 13         LOADL        1
 14         CALL         fieldref
 15         LOADL        1
 16         LOAD         3[LB]
 17         CALL         fieldupd
 18         LOAD         3[LB]
 19         LOADL        1
 20         CALL         fieldref
 21         LOADL        1
 22         CALL         fieldref
 23         LOADL        0
 24         LOADL        7
 25         CALL         fieldupd
 26         PUSH         1
 27         LOAD         3[LB]
 28         LOADL        0
 29         CALL         fieldref
 30         STORE        4[LB]
 31         LOAD         4[LB]
 32         CALL         putint  
 33         RETURN (0)   0
 34  L11:   LOADL        -1
 35         LOADL        1
 36         LOADA        1[CB]
 37         JUMP         L12
 38  L12:   LOADL        -1
 39         LOADL        0
 40         JUMP         L13
 41  L13:   LOADL        -1
 42         LOADL        0
 43         LOADL        -1
 44         CALL         L10
 45         HALT   (0)   
