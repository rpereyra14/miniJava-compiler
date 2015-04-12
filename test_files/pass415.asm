  0         JUMP         L11
  1  L10:   PUSH         1
  2         LOADA        3[SB]
  3         LOADL        2
  4         CALL         newobj  
  5         STORE        3[LB]
  6         LOAD         3[LB]
  7         LOADL        1
  8         LOADA        8[SB]
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
 19         CALL         L12
 20         RETURN (0)   0
 21  L11:   LOADL        -1
 22         LOADL        1
 23         LOADA        1[CB]
 24         JUMP         L14
 25  L12:   PUSH         1
 26         LOADL        10
 27         STORE        4[LB]
 28         LOADA        0[OB]
 29         LOADL        0
 30         LOADL        4
 31         CALL         fieldupd
 32         LOADA        0[OB]
 33         LOADL        1
 34         CALL         fieldref
 35         LOADL        0
 36         LOADL        5
 37         CALL         fieldupd
 38         LOADL        2
 39         LOADA        0[OB]
 40         LOADA        0[OB]
 41         LOADL        1
 42         CALL         fieldref
 43         LOADA        0[OB]
 44         CALL         L13
 45         CALL         add     
 46         CALL         putint  
 47         RETURN (0)   0
 48         LOADA        0[OB]
 49         LOADL        0
 50         CALL         fieldref
 51         LOAD         -1[LB]
 52         CALL         add     
 53         LOAD         -2[LB]
 54         CALL         add     
 55         RETURN (1)   2
 56  L13:   LOAD         -1[LB]
 57         LOADL        0
 58         CALL         fieldref
 59         LOAD         -2[LB]
 60         LOADL        0
 61         CALL         fieldref
 62         CALL         add     
 63         LOADA        0[OB]
 64         LOADL        0
 65         CALL         fieldref
 66         CALL         add     
 67         RETURN (1)   2
 68  L14:   LOADL        -1
 69         LOADL        3
 70         LOADA        25[CB]
 71         LOADA        48[CB]
 72         LOADA        56[CB]
 73         JUMP         L15
 74  L15:   LOADL        -1
 75         LOADL        0
 76         LOADL        -1
 77         CALL         L10
 78         HALT   (0)   
