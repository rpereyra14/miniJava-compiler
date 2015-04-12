  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADL        0
  3         STORE        3[LB]
  4         LOADL        6
  5         STORE        3[LB]
  6         PUSH         1
  7         LOADA        3[SB]
  8         LOADL        2
  9         CALL         newobj  
 10         STORE        4[LB]
 11         LOAD         4[LB]
 12         LOADL        1
 13         LOADA        5[SB]
 14         LOADL        2
 15         CALL         newobj  
 16         CALL         fieldupd
 17         LOAD         4[LB]
 18         LOADL        1
 19         CALL         fieldref
 20         LOADL        1
 21         LOAD         4[LB]
 22         CALL         fieldupd
 23         LOAD         4[LB]
 24         LOADL        1
 25         CALL         fieldref
 26         LOADL        1
 27         CALL         fieldref
 28         LOADL        0
 29         LOAD         3[LB]
 30         LOADL        1
 31         CALL         add     
 32         CALL         fieldupd
 33         LOAD         4[LB]
 34         LOADL        0
 35         CALL         fieldref
 36         STORE        3[LB]
 37         LOAD         3[LB]
 38         CALL         putint  
 39         RETURN (0)   0
 40  L11:   LOADL        -1
 41         LOADL        1
 42         LOADA        1[CB]
 43         JUMP         L12
 44  L12:   LOADL        -1
 45         LOADL        0
 46         JUMP         L13
 47  L13:   LOADL        -1
 48         LOADL        0
 49         LOADL        -1
 50         CALL         L10
 51         HALT   (0)   
