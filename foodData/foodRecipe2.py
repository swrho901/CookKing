import csv
import sys


f = open('G:recipeData\\foodData1.csv', 'r')
ff = open('G:recipeData\\result.txt', 'w')
rdr = csv.reader(f)

idx = 0;

makeIdx = [-1, 30, 42, 43, 40, 41, 38, 39, 36, 37, 47]

for line in rdr:

    #메뉴명, 메뉴 사진, 메뉴 idx
    ff.write("{\"" + line[53] + "\", \"" + line[44] + "\", \"" + str(idx) + "\"}, \n")

    newRecipe = line[4].replace("\n", ",")
    newRecipe1 = newRecipe.split(",")
    L = len(newRecipe1)

    ff.write("{")
    for j in range(0, L):
        ff.write("\"" + newRecipe1[j] + "\"")
        if j == L - 1:
            1
        else:
            ff.write(", ")
    ff.write("},\n{")

    index = 1
    
    for i in range(1,11):
        if len(line) == 0:
            index = i - 1
            break
        index = i
    
    for j in range(1, index):
        str2 = line[makeIdx[j]].replace("\n","")
        ff.write("\"" + str2 + "\"")
        if j == index - 1:
            1
        else:
            ff.write(", ")
    ff.write("},\n")

    index = 1
    
    #마지막 것만 쉼표 없애면 될
        
    #다 하고 newRecipe1 초기화??
    #ff.write(newRecipe)
    
    print("음식 이름 : " + line[53] + "\n필요한 재료 :  " + newRecipe)
    print("최종 완성 그림 url : " + line[44])
    print("1번째 조리방법 : " + line[30])
    
    print("")
    
    idx = idx + 1



f.close()
ff.close()
