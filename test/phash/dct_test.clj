(ns phash.dct-test
  (:require
   [clojure.test :refer :all]
   [phash.dct :refer :all]
   [phash.utils :as u]
   [phash.test-utils :as tu]
   [clojure.test.check.generators :as gen]
   [clojure.test.check.clojure-test :as ct]
   [clojure.test.check.properties :as prop]))

(defonce test-vals (-> tu/compr
                       first
                       (u/resize ,,, 32 32)
                       u/grayscale
                       u/get-pixels))

(defn- close-to-equal [as bs epsilon]
  (transduce
   (comp
    (map (fn [[a b]] (if (= a b) 0 (- a b))))
    (map #(Math/abs %))
    (filter (fn [diff] (< epsilon diff))))
   (fn
     ;; Empty seqs are equal
     ([] true)
     ;; If we have not found an offending element we are 'close enough to equal'
     ([result] result)
     ;; We only need to look for one offending element
     ([_ _]
      (reduced false)))
   (mapv vector as bs)))

(defonce ^:private epsilon 0.000001)

(deftest slow-dct-32x32-test
  (testing "slow dct 32x32 test"
    (is (close-to-equal
         [-7.812549304374999E8 -3.351494902536385E8 2.0437986597320594E7
          -1.091967597755367E8 1.2186901380363176E7 -1.020073710193079E8
          1.3649311399257267E7 -5.817265120760768E7 2.9486463175824475E7
          -4.562329083262037E7 -1.618158751764673E7 2.4731922637231026E7
          -1.5797959878087854E8 -290025.5239636314 8929253.6917964
          -2.493742185767064E7 1365483.440729869 -2.0840897248597424E7
          2.1044306323364936E7 -2.57043038266545E7 1.4689772515959289E7
          -5218066.790376874 -4.275918278707747E7 -9156624.52822635
          2.2312197305431306E7 -7602541.59940721 -1.3216207412148478E7
          486544.0965669562 3523058.5473164557 -1.625088361903439E7
          9970900.471854296 -2.589220759276411E7 -4.2344855209221375E8
          -1.9167963473029855E8 -6.547105431061454E7 -8.556332830314694E7
          -1.900700592678937E7 -7.862944127002539E7 2.8689110773017313E7
          -3.49397917236522E7 -9666219.639961846 -3.325429960687972E7
          -1.999581025028678E7 1.7741808700196497E7 -7.909809486888866E7
          -1.5830164105421994E7 3910102.8290695767 -1.474027836161503E7
          -825785.6232672774 -2.2383271790645473E7 3403679.125547895
          -2.348289358226971E7 8495571.828746174 -7615675.463727277
          -2.508819359594227E7 -1.093427191944641E7 1.0473571259355156E7
          -4724742.319029234 -1.0682462392804107E7 -163511.74075475265
          -757238.5455956091 -1.7966783195929546E7 4822358.134342206
          -1.8729274940786898E7 -1.7077066074302438E8 -7.53162736824552E7
          -1.5488791930540917E7 -3.205300247061288E7 8451688.96255617
          -1.9337897598608296E7 1.2046982786879534E7 -1.001199652525241E7
          -2379297.8404767634 -1.163182524936563E7 2338067.6994291707
          1.0267771889187053E7 -2.843481669240292E7 -3300261.431988237
          -5100667.611307647 -2348038.9411903517 2584305.1318960628
          -5222762.05450465 8459749.244640414 -5979825.621790157
          3139864.0019861883 -1809002.0211384161 -1.2428765734310366E7
          -1218118.7264625984 7633044.438501474 -3488556.5319812116
          -2924478.9579179045 -6192293.593353154 -364686.15156070015
          -93859.46288235311 -488980.5799244116 -1951007.4030854525
          -4.181351811596495E7 -5166548.31895872 8.289073851383296E7
          1.4291314585294636E7 2.3074515461959504E7 1.4286360302277386E7
          -3.69159912619901E7 -2108644.521084308 2.0351547591837585E7
          655533.6247832854 1.0845214143957268E7 -8966828.403402533
          -1.9949241459217463E7 1.3323531550968884E7 -3744624.4215943995
          69039.0515854226 1187385.0881844568 8074266.142575654
          8655789.612455543 6736178.316200402 -3446561.63626918
          5265491.286021103 -3970686.408754374 8941538.534250919
          3402932.050046238 -2600960.0396949016 2324980.0433598296
          -4055978.491619587 486722.8430622375 1.0490934416215248E7
          -1502965.8490746664 3121211.8353339797 -3.437686386696428E7
          -2.5740227877318654E7 -2.508681391719352E7 -1.2727804444309603E7
          -3.3359384794342924E7 -1.8301880197797332E7 2457164.7239359785
          -5588198.42949544 -1.221432554505218E7 -9405543.99419159
          -1542546.6392895305 5566947.28339804 -5535959.354676569
          -1.2366578300835654E7 -2572345.1913231993 1215408.1666508303
          503516.3510314647 -9498801.180328617 -3645973.180532722
          -1669017.5035681236 -1609128.3680959826 -2783177.639188816
          -3785169.96975144 -4124365.09900567 3195135.8369715624
          2363041.537398831 -6641202.25653676 -1424661.091271907
          -3082952.4204082973 -5347550.3651247285 202848.0398745013
          -3232205.5321601406 -1.5862812695414916E8 -6.844673397957641E7
          -843401.0909165775 -1.8253752956417844E7 1.6917920199654453E7
          -9198559.350786008 1.6974832419445068E7 -1.1579314302565522E7
          1.3645817543647546E7 -2232164.785970129 -2937286.260532424
          7487893.745763027 -2.856074299214256E7 6802786.536079105
          3565312.748678151 -7933111.564574221 1248146.302434738
          1893966.7052583913 7762358.864384252 -5731466.858952691
          3907329.003101941 -1526265.4647967292 -4513785.418933143
          -2208955.5973892505 3744365.745554818 -1173209.8191947257
          710418.723930481 1652977.1512857527 1774092.8483269047
          -3085767.692558849 3442575.250267909 -3268949.7852681903
          2.684629403229887E7 1.917887530719194E7 2466455.8762135496
          2683531.613366487 9873086.72714735 -3801046.0891083404
          -1.8735205948571168E7 -641735.7842608448 -1367972.3574692397
          -3692376.0662678336 -2155224.5855849255 -3536175.7670730655
          -1787938.1793042594 -2519682.5497151134 4529938.540571022
          -1426899.133076997 -1087029.0563051805 -2127648.4267923855
          -5078552.12821556 454613.89730744914 -1641097.5537364394
          -401430.7909119789 740838.0888893277 1735373.663266235
          -283979.65425080695 -2881793.645059854 -1717708.7823798126
          2085067.1232125205 2133857.7170051243 -887811.7419515909
          1263471.9215794513 -2453889.8262124723 -7.53671829724734E7
          -4.052464817719429E7 1.152250694196331E7 -4303895.5981563255
          -1.8091961968353715E7 -9165544.9367021 1.2429399817548044E7
          -993480.5597432326 -44463.5152027554 -3838566.344914469
          4950059.5925106285 1.0207263159251863E7 -8532283.172105497
          -5473170.343855477 -4574177.199036299 3330557.8700240785
          2149919.009216688 -4029777.3738759058 3524069.1746345167
          4100782.384006173 1333296.0370703465 -140431.6040448228
          -5965002.81912748 -3292346.414447771 5016517.738200101
          5150793.019502047 -5928064.830109908 -2224750.6781304874
          -1212023.3720524702 -410253.8937544973 640093.3158158151
          175386.9190642297 1.4043526642534584E7 5626908.948247224
          -2.0372414952700853E7 -6601989.583585128 3927254.174507368
          1345710.8220020654 -970672.354456975 -4553975.680081227
          -3517428.5128626036 823461.2668012441 -6060956.530181685
          -7613883.198156274 -628337.0015097994 2499400.400623092
          -971335.6063005514 -6121892.996152429 -2181179.474991343
          2632576.615361468 -1153657.5549049717 -6580074.165205387
          -2681338.1733527053 -1085135.5915165986 2198768.425423507
          2868855.470047382 -2608080.076036921 -3228637.353709928
          3538933.4726283555 -177645.74029949523 -1998201.0518508803
          -1706861.6045557505 -898290.7229727039 -1352673.334542241
          -6.053756435385629E7 -2.415597803089762E7 7399864.611860942
          -1251071.5563726202 5045433.530336821 -9856878.61995188
          -4521856.150115825 1413925.8327943825 8353011.181753117
          -5809885.453418343 4165603.8634820194 7060325.274446543
          -1.1564072939344674E7 -1794573.2702123849 5870955.823633261
          1635884.3230956004 2494961.34944856 -5175648.776114502
          1746559.5981360953 1341424.2289266698 4834626.8254891485
          587132.7832218632 -5088551.276863049 463825.69946559146
          5376577.720366166 -1696656.9071822753 -3540105.3843797236
          2964874.53417118 2516417.432305565 247895.83757396572
          1285711.358978193 -2097152.9218065855 4997154.0859531155
          3546271.614437935 1.318509726534066E7 -478167.08188750094
          455597.58640722814 6695942.795386773 9250175.16924737
          418586.7259679987 -1299406.8787282445 4508213.408525612
          -2835068.385865446 -3238179.21187331 3679134.154443391
          5510190.491607018 -4629528.523964173 644854.8622392546
          -1155865.936600458 3487406.9484385075 2152979.6690626023
          -398454.2052753399 -740246.8812307934 1702860.377385277
          513371.5260470824 -1182453.5304713435 -4376305.968028866
          1778109.5874493078 4330139.394759611 -2252083.044724012
          -1110761.0928136846 1999946.0780475172 -1674448.365976182
          2369819.878906114 4.414537133205837E7 2.256023690322221E7
          -1.1687016908415753E7 817652.7840675218 5566813.212662206
          3131482.7396867545 -1.193124393862066E7 550575.7036091565
          -836450.3167270378 2906213.330958522 1897124.7082402878
          -3465302.1623078026 4811495.624017118 1451896.8279993772
          3663902.2283867635 -2275619.2762160935 548012.4562973325
          3647210.707388455 -1058916.3659578976 -1640821.7235679948
          -579818.6461188621 -2890806.2103932393 3141811.33181577
          3381481.1372485836 105108.13016889876 -2831883.0946602295
          1331422.218119874 1058024.4763521282 605.9896234589396
          610705.371943116 594175.2846981261 397108.11076605006
          -1.679050740570823E8 -7.427631519823922E7 1193552.4300907666
          -2.0374878570010703E7 1115695.0622115568 -2.3588610846696127E7
          8463377.0308552 -9236533.97587919 7273850.471502926
          -1.1970859684091074E7 -2719314.605847179 7143826.413799894
          -3.2263099461668223E7 -5389055.65185261 1779696.1287913043
          -3410467.6987128155 -539611.6929839558 -7881468.58493424
          2861108.1895539705 -3717966.884702415 3440168.336843647
          567944.4333109631 -1.0015187425738342E7 -3266955.1396443644
          5645537.67561834 -637982.7227233985 -5468452.046814214
          217022.63549736358 1732791.2819207597 -3610167.355442852
          2117084.1998403696 -6059947.787900394 -4.036224347580683E7
          -1.837982072558832E7 872518.199279988 -9189615.94812736
          101321.32725831738 45397.2552666734 5731883.3670486
          -7601881.138187309 -3079631.059981907 -72090.8214457559
          -4467463.655036774 -1211803.1848252574 -5708526.894049555
          6436262.958565434 -3748198.4407455623 -1114310.8249764156
          -44947.789005696715 1840876.461574918 3024557.449396356
          -3175002.3372604777 -169661.41029782937 630490.3813753319
          -232148.67680468666 -2177198.0748363654 -3055281.380079613
          -202461.89418908482 3088161.681317607 -1940837.6923934636
          -832734.5741303903 -1979406.3715571065 -926938.613601889
          -1062347.9101217939 1.331466045456181E7 6754453.998049194
          -1.2534005913376419E7 -682613.557923472 28010.936837066314
          -4365051.264411829 3628898.754817159 4506352.733440273
          -323903.10177356796 249515.12753880734 -516306.27301266516
          2982844.1343300324 3299240.9953935547 -3282234.3454843783
          4143379.914885452 -1709772.8371706244 519062.248211317
          -1457355.6393352898 -2088163.9868577572 -580480.7677882105
          1422222.2410185053 -2009147.9808637407 -72504.35527784185
          667822.7833945772 1644258.4126633415 -177090.07165338955
          -923199.4315893576 2914331.0564718232 -152596.62123725883
          -594455.1838550494 1142354.2459022547 91521.65996122477
          -3.2062710336400848E7 -1.6034914142455487E7 1305314.1540642662
          -7491370.98598904 -2901340.9734650697 -2853472.5812391257
          -981581.846622441 -6792951.193996506 -3619295.625591951
          -2831975.567394857 -2340618.4544217065 -2602845.524073895
          -7373717.940778576 -1655210.855821146 -4289283.182071064
          -1118687.8819781851 -1969129.7770740457 -269488.3919606622
          -246584.26687813425 -1275126.7055291773 -2277416.9878150853
          78423.1264796877 -901462.6066731922 -2654379.3327161334
          -1283691.7130701952 700114.7362123238 -1196952.8540995352
          -3257325.3458846817 -131740.3204458009 -1723659.3007291527
          -234065.08386851184 -1727275.19758461 -1.0191419356575662E7
          -5287418.918111038 -563709.6607658203 363733.2789593962
          -7787851.776066077 -1953540.3978922544 2972474.6790867886
          -1175048.4237203943 1888684.3916687476 1130389.2423834575
          1152281.0145657775 1211441.3168767425 -340365.09833146574
          2397852.12208493 556633.9269857015 2034184.0746931129
          3431769.7339307806 -2273063.6626548613 1281375.335797188
          -3162099.965359042 736584.870909083 15646.120251395041
          -433994.8510078406 809626.3660345761 461593.2735912747
          841937.3051395665 812427.7570693361 1254374.0571240513
          -1477907.3744766514 157695.88778248813 533579.7512836319
          1220808.741466043 -4.6211778006233625E7 -2.2238386488714382E7
          -6623461.342490487 -5924981.104649906 4563089.33065362
          -9288264.807632202 5706229.109936259 -165884.62976940777
          728590.9450071342 -5796352.292102987 -3419966.6528528756
          5799338.006908642 -8201516.127530207 -4996233.225011789
          763572.9481158378 -4124556.422543727 -2489144.330399731
          -3494886.7082793512 -396437.10924548656 970444.8724994666
          1072224.2725228835 -782791.1754942145 -3480645.3096499736
          -1750311.831304403 3450947.547637078 -500370.4232098646
          -3155732.6433053617 2007799.1996193598 812007.902250107
          -2665448.577710403 201285.75273706293 -3147470.984439646
          2.6615521432399996E7 1.31274996705245E7 1.031257231370213E7
          5502130.950284183 -1743879.1853351279 1.0164556163093202E7
          -5088855.021366332 -1164483.7732413877 1876468.354134888
          5216116.310924084 3824395.3587230896 -2449025.396468355
          5214570.962517938 3640634.115159056 -1032595.2205892614
          3010447.160458056 2194783.9332867754 4384738.669351271
          1492045.0205779225 1750938.8541484897 -1970780.3014915371
          192898.60274366048 4254485.89430434 -516223.4833896856
          -2058212.9590742956 1451995.1072747037 1921788.5248923718
          -1817834.1321049505 -544083.5398173442 1775212.7139554003
          69935.37515086248 2238818.355579941 -5.323984134396899E7
          -2.4733197849883776E7 -1611736.554298971 -9246436.688936206
          -1671060.7480838504 -1.0921172495943949E7 4288567.722756565
          -1448697.0346605314 -2023016.219951237 -5732839.0891546365
          -3132551.396852642 1202860.5911017132 -9644749.747846447
          -1738478.2375638306 -2802045.422670859 -1895500.8556336884
          -553737.9306761887 -4909851.402603745 579189.651241114
          -3558076.9666448943 383720.7984449162 201261.59881575214
          -5897152.464312671 1330299.9961625566 2298901.382046561
          -951322.3444583098 -1534607.968824928 147747.67561819163
          -1591894.758375751 -729005.8806335944 -706555.2058202364
          -1487402.7511447528 1.0453623066208983E7 4912812.584610456
          -1398394.5301901125 59342.42885038443 1687837.5239982146
          3003161.5727883168 -5386968.999842474 -2718167.610524762
          -514723.8950276668 600711.9642682634 610186.1206634762
          -232200.39408474317 308317.07349165436 179324.62479987426
          685601.441236627 -474220.61750899866 -1444807.0187908402
          2805484.7889231173 792253.7227945972 1672099.7972952765
          -303650.4262222657 -697232.5518123481 1675886.611685992
          -906567.7137563457 -708680.8487030015 -506079.9898821557
          620038.9390851523 -1402593.6310028196 1106545.1956594838
          -339479.2783051804 -36655.81192860304 -527634.046124326
          -1.4263998075175358E7 -5280241.970628717 -406974.16973832075
          -4064351.3209994715 -2919449.9718584167 -172989.3425135439
          3544334.962634733 -2485768.698756217 947901.1042021513
          1206553.1592522294 -193096.57878080005 -985439.0203899493
          -1465903.8406929062 1471643.7073780145 -561568.4289697494
          430428.65536910464 2284273.8860558905 -1246886.7480681138
          106547.3145619802 -3613679.3600102286 -840176.8482301269
          -1068349.8965835243 919208.9210812182 -1825894.9312981062
          -926002.5264477287 881450.1962016006 1256306.0407493813
          -75285.5699451364 -1305052.1955533868 -246537.06575081783
          396410.15928590606 1015067.5648957503 -2.7661460521218505E7
          -1.2621632128128953E7 7582984.432202068 -2259043.669345176
          2222321.6769691966 -2772984.137827035 -1354213.4499820466
          58849.76531263132 -510741.03293342877 -2697228.395874413
          -804375.0298752061 -319221.73802152637 -6402105.25877531
          1936126.6095556247 -608477.5952931948 -1700911.803354345
          -1339324.1914044837 757462.9299114984 790569.6909856289
          1530653.2795347178 1157926.9926555492 1389728.4457654394
          -2969227.2514639706 1861367.878977263 1133481.8739248458
          -1459397.5656505323 -1139405.2311169738 -143328.8985329138
          510653.04627688177 -215268.41656192642 -622667.494754649
          -1740947.2477501342 -1.0034514585747575E7 -2739311.695188161
          -4530778.715809544 -4679266.35484369 906100.7055269293
          615223.2947275746 2330994.354150963 -3833695.1266508517
          2921639.406299487 2181895.064556311 -1518349.0216021384
          -634868.3922138752 -2220337.6107571973 1808871.821326462
          1438398.493395939 -550797.6781600486 45033.29062602998
          1584941.3413825284 1419636.7162058775 -3144032.028566727
          -112170.62149532876 -1083036.4987553542 1244629.3152253735
          -2300503.564537862 -1591426.6120126825 158303.19638271202
          3023975.643222745 -390398.85872316017 145205.18490186826
          868909.4032326979 927852.5516468417 805791.7980557987
          3.9888948490892865E7 1.603879126564056E7 2.0043580837358262E7
          1.5016912473949935E7 -1526839.0611110171 7814467.41659104
          -648791.2985827025 4610160.166355591 881373.3949222341
          3637353.55460919 2016985.8784046052 -748354.8567003261
          9762640.41659132 928386.5682095082 -4061080.6217093775
          2178814.2174773323 751213.8511889107 67108.8876357651
          -77030.44331494087 3893471.7822502544 -1806545.4374191328
          1437217.9607362156 2022189.460094805 1380326.4706105022
          -869422.6877601665 3482869.181554242 -602694.715489769
          732482.059360543 -1509281.1973924222 611956.9710182186
          -1084374.5553068852 1711803.3297535386 -1.0322812936608005E7
          -338972.58691501385 -2893366.038720837 -4565563.6546148835
          3363671.5895045237 -1599017.9935608804 -6383777.414499229
          -280345.35389329225 1628196.7037644072 -1697719.3296814088
          3533119.153909597 -1409453.1757322233 -4858571.728438971
          1892689.609219879 3393154.05708468 668176.9173818874
          984585.9423864551 969043.4496987131 1030317.3508631762
          -573657.1563507458 2372577.052778284 254213.47539700076
          -2078469.6374842722 3039583.46868044 1359636.2031697684
          -3737919.0129928268 502607.1458942445 -1301453.5845753571
          1306372.4020235217 2616436.62733765 341324.35626700614
          287711.1076752885 -3.3111566413232394E7 -1.779841364542E7
          -1.5010150960915394E7 -9799199.447846625 -1030282.7555501624
          -7319665.591719101 4678493.525337807 -3097151.2951491065
          -1647533.8883789452 -3395443.8494653353 -5156591.555634837
          2577600.727106449 -4734382.0704248585 -1839916.6868357286
          576568.3333672005 -3099527.824417251 -1384360.9706676612
          -2965156.348996813 -480683.36110227916 -4236365.42522636
          227587.14699766928 -1783797.6811152278 -961792.960951516
          -3770872.357867276 547087.3773587075 -741653.3564780544
          279952.1839437356 2255103.4357478074 -673340.093971525
          -2869543.4256820935 -259734.44906739797 -2300785.188486334
          6462124.542421029 5447708.247811278 4219386.461491512
          3936181.023676484 -730670.724844465 -772022.6540817464
          -3085034.0924120364 273592.5051280238 -677586.4130502292
          823883.8651513057 901853.2526299637 -567873.7595618726
          511988.3374947169 -270071.6823631795 205726.15703797463
          467677.40294980316 2357228.06075976 166519.90200515138
          -451828.96605080826 986838.74056698 -987560.4705170596
          292984.2376300835 168082.7078657139 3587466.9367964873
          375311.55919344514 1037980.4390788785 -1646289.126698042
          -210065.93825821683 -741988.8174322038 -768279.4901611931
          888400.8090903254 -346200.1716807771 -8147669.966341418
          -3755046.507434117 -3382130.8843113063 -4966286.529874414
          178939.45675114257 -2575050.602494072 127957.1264668107
          -1668067.837081874 964547.3403393191 -511351.754579616
          -293599.1357534559 -885744.2308030933 -3345494.1040733643
          484370.0522196917 609463.7184035964 -1014745.8468835263
          -2213756.523121538 -413003.5498817652 -60863.20902851425
          -1753570.2455802632 1721942.2584764562 -1509278.2991924586
          -1034777.5139980579 -1599114.651940071 -482038.44878102094
          -1142697.5048924473 1276650.1727301865 8913.761307070497
          1236654.0048609306 616884.1042436098 75237.95436200278
          726644.9825097932 -2.7754865992620103E7 -1.5297405164243894E7
          3154210.332145322 402492.35758279706 -4825508.644208003
          -1623259.3868316896 2437311.451166724 -335501.53852094687
          -383984.18221949344 -1553139.2548308682 -451153.90980497247
          3774890.951654191 -1568448.8590857945 -525815.9131326118
          -2156579.559253691 1024716.7605509583 1750660.7994239174
          -1177744.13121554 2578412.6878479254 846988.8300751125
          -988622.6787021944 1339403.2353379913 -695230.7890994017
          -105670.00803568435 1508646.8280252316 1354547.655439864
          -1680482.2443286472 737051.187127549 -2358000.254311023
          -1494374.8283392917 -720974.5443192617 -1930784.6498951986
          5716776.008100541 2855273.602124625 -7778286.738329449
          -3440163.996656484 -1108610.261810512 408843.7890409378
          529679.1723251848 -2509339.943164439 -2154257.498500143
          160323.44677974947 1936373.2878129864 -278224.75101390923
          -2266853.0003008116 214004.95066526486 2524459.273535092
          -1387090.9794099708 558739.1494682971 1363669.38300965
          -2833928.464088832 -527101.7145278899 336924.6597003808
          -1767171.346488304 831938.4664281996 -95202.17157384969
          285654.52979045187 -977367.6198160277 -481970.0753114834
          -2330869.517719626 2128824.045021313 -391598.2839196953
          2107245.4938562647 1032828.3486033143 -3.151093430452423E7
          -1.3036218062046004E7 3580193.4372703265 -4072621.1850490132
          1185132.0504703785 -5025214.990094695 -2662293.009859359
          -2677013.0875639413 2349888.7089179787 -1867934.258407324
          -956438.9841136076 121800.3829577344 -6392422.733412711
          -541390.8090790376 -900850.1710116904 -464140.04503965506
          -776865.7954787503 -1451295.6400787488 1990334.5220360255
          -1185587.8668014735 468594.2609841382 -98940.01506611158
          -2257048.0385436295 -686857.9687876707 656731.9491239182
          -120799.42999629723 102899.90353549962 460675.7806016289
          -453754.5307927922 141241.59375047416 -219124.21988855093
          -1067915.7365263023]
         (discret-cosine-transform-32x32 test-vals)
         epsilon))))

(deftest slow-dct-32x32->8x8-test
  (testing "slow dct 32x32 + reduction to 8x8 test"
    (is (close-to-equal
         [-7.812549304374999E8 -4.2344855209221375E8 -1.7077066074302438E8
          -4.181351811596495E7 -3.437686386696428E7 -1.5862812695414916E8
          2.684629403229887E7 -7.53671829724734E7 -3.351494902536385E8
          -1.9167963473029855E8 -7.53162736824552E7 -5166548.31895872
          -2.5740227877318654E7 -6.844673397957641E7 1.917887530719194E7
          -4.052464817719429E7 2.0437986597320594E7 -6.547105431061454E7
          -1.5488791930540917E7 8.289073851383296E7 -2.508681391719352E7
          -843401.0909165775 2466455.8762135496 1.152250694196331E7
          -1.091967597755367E8 -8.556332830314694E7 -3.205300247061288E7
          1.4291314585294636E7 -1.2727804444309603E7 -1.8253752956417844E7
          2683531.613366487 -4303895.5981563255 1.2186901380363176E7
          -1.900700592678937E7 8451688.96255617 2.3074515461959504E7
          -3.3359384794342924E7 1.6917920199654453E7 9873086.72714735
          -1.8091961968353715E7 -1.020073710193079E8 -7.862944127002539E7
          -1.9337897598608296E7 1.4286360302277386E7 -1.8301880197797332E7
          -9198559.350786008 -3801046.0891083404 -9165544.9367021
          1.3649311399257267E7 2.8689110773017313E7 1.2046982786879534E7
          -3.69159912619901E7 2457164.7239359785 1.6974832419445068E7
          -1.8735205948571168E7 1.2429399817548044E7 -5.817265120760768E7
          -3.49397917236522E7 -1.001199652525241E7 -2108644.521084308
          -5588198.42949544 -1.1579314302565522E7 -641735.7842608448
          -993480.5597432326]
         (reduce-dct-32x32->8x8
          (discret-cosine-transform-32x32 test-vals))
         epsilon))))

(deftest fast-dct-32x32->8x8-test
  (testing "fast dct with reduction test"
    (is (close-to-equal
         [-7.812549304374999E8 -4.2344855209221375E8 -1.7077066074302438E8
          -4.181351811596495E7 -3.437686386696428E7 -1.5862812695414916E8
          2.684629403229887E7 -7.53671829724734E7 -3.351494902536385E8
          -1.9167963473029855E8 -7.53162736824552E7 -5166548.31895872
          -2.5740227877318654E7 -6.844673397957641E7 1.917887530719194E7
          -4.052464817719429E7 2.0437986597320594E7 -6.547105431061454E7
          -1.5488791930540917E7 8.289073851383296E7 -2.508681391719352E7
          -843401.0909165775 2466455.8762135496 1.152250694196331E7
          -1.091967597755367E8 -8.556332830314694E7 -3.205300247061288E7
          1.4291314585294636E7 -1.2727804444309603E7 -1.8253752956417844E7
          2683531.613366487 -4303895.5981563255 1.2186901380363176E7
          -1.900700592678937E7 8451688.96255617 2.3074515461959504E7
          -3.3359384794342924E7 1.6917920199654453E7 9873086.72714735
          -1.8091961968353715E7 -1.020073710193079E8 -7.862944127002539E7
          -1.9337897598608296E7 1.4286360302277386E7 -1.8301880197797332E7
          -9198559.350786008 -3801046.0891083404 -9165544.9367021
          1.3649311399257267E7 2.8689110773017313E7 1.2046982786879534E7
          -3.69159912619901E7 2457164.7239359785 1.6974832419445068E7
          -1.8735205948571168E7 1.2429399817548044E7 -5.817265120760768E7
          -3.49397917236522E7 -1.001199652525241E7 -2108644.521084308
          -5588198.42949544 -1.1579314302565522E7 -641735.7842608448
          -993480.5597432326]
         (discret-cosine-transform-reduced-32x32 test-vals)
         epsilon))))

(defonce ^:private dct-values-gen (gen/vector gen/nat (* 32 32)))

(ct/defspec discret-cosine-transform-oracle-test 20
  (prop/for-all [dct-vals dct-values-gen]
                (= (reduce-dct-32x32->8x8
                    (discret-cosine-transform-32x32 dct-vals))
                   (discret-cosine-transform-reduced-32x32 dct-vals))))