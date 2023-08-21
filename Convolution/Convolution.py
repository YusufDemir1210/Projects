
print('Please enter the name of the image')
filename = input()

print('Please choose your convolution operation')
operation = int(input())


def img_printer(img):
    row = len(img)
    col = len(img[0])
    cha = len(img[0][0])
    for i in range(row):
        for j in range(col):
            for k in range(cha):
                print(img[i][j][k], end=" ")
            print("\t|", end=" ")
        print()

# OPERATION 1
if operation == 1:
    inp = open(filename)
    for _ in range(2):
        trash = inp.readline().split()
    n_column,n_row = int(trash[0]), int(trash[1])
    resolution = inp.readline()

    content = inp.read().split()
    image = []
    i = 0
    for _ in range(n_row):
        row = []
        for __ in range(n_column):
            row.append([content[i]])
            i += 1
        image.append(row)
    inp.close()

    region = []
    def region_finder(img,x,y):
        global region,n_row,n_column
        if img[y][x][0] == "0" or (x,y) in region:
            return
        region.append((x,y))
        neigh = [(x+1,y),(x-1,y),(x,y+1),(x,y-1)]
        for coordinate in neigh:
            if coordinate[0] >= 0 and coordinate[0] < n_column and coordinate[1] >= 0 and coordinate[1] < n_row :
                region_finder(img,coordinate[0],coordinate[1])
    region_finder(image,0,0)

    all_regions = [[]]
    for x0 in range(n_column):
        for y0 in range(n_row):
            logic = False
            for reg in all_regions:
                if (x0,y0) in reg:
                    logic = True
            if not logic:
                region = []
                region_finder(image,x0,y0)
                if not region == []:
                    all_regions.append(sorted(region))
            else: continue
    all_regions = all_regions[1:]

    d_colors = {}
    for reg in all_regions:
        sum = 0
        for coordinate in reg:
            x,y = coordinate[0],coordinate[1]
            pixel = int(image[y][x][0])
            sum += pixel
        average_color = str(sum//len(reg))
        for coordinate in reg:
            d_colors[coordinate] = average_color

    colored = []
    def recursive_coloring(x,y):
        global colored, d_colors, image
        if image[y][x][0] == "0":
            return
        image[y][x][0] = d_colors[(x,y)]
        colored.append((x,y))
        neigh = [(x+1,y),(x-1,y),(x,y+1),(x,y-1)]
        for coordinate in neigh:
            if coordinate not in colored:
                px,py = coordinate[0],coordinate[1]
                if px >= 0 and px < n_column and py >= 0 and py < n_row:
                    recursive_coloring(px,py)

    start_points = []
    for reg in all_regions:
        start_points.append(reg[0])
    for (x1,y1) in start_points:
        colored = []
        recursive_coloring(x1,y1)

    img_printer(image)


# OPERATION 2
if operation == 2:
    filter_name = input()
    stride = int(input())

    inp = open(filename)
    for _ in range(2):
        trash = inp.readline().split()
    n_column, n_row = int(trash[0]), int(trash[1])
    resolution = inp.readline()

    content = inp.read().split()
    image = []
    temp_matrix = []
    i = 0
    for _ in range(n_row):
        row = []
        trash = []
        for __ in range(n_column):
            channel = []
            for ___ in range(3):
                channel.append(content[i])
                i += 1
            row.append(channel)
            trash.append("--")
        temp_matrix.append(trash)
        image.append(row)
    inp.close()

    filter = open(filter_name)
    kernel_matrix = []
    for line in filter:
        line = line.split()
        row = []
        for el in line:
            row.append(float(el))
        kernel_matrix.append(row)
    m = len(kernel_matrix)
    filter.close()


    already_done = []
    def convolution(x, y):
        global m, n_row, n_column, temp_matrix, stride, image, already_done
        if x + m - 1 >= n_column or y + m - 1 >= n_row or (x, y) in already_done:
            return
        pixel = []
        for z0 in range(3):
            weighted_sum = 0
            for x0 in range(m):
                for y0 in range(m):
                    n = int(image[y + y0][x + x0][z0])
                    factor = kernel_matrix[y0][x0]
                    weighted_sum += n * factor
            if weighted_sum < 0:
                weighted_sum = 0
            elif weighted_sum > int(resolution):
                weighted_sum = int(resolution)
            weighted_sum = int(weighted_sum)
            pixel.append(weighted_sum)
        temp_matrix[y][x] = pixel
        already_done.append((x, y))
        convolution(x + stride, y)
        convolution(x, y + stride)

    output_matrix = []
    convolution(0,0)
    for temp in temp_matrix:
        lst = []
        for channel in temp:
            if channel != "--":
                lst.append(channel)
        if lst != []:
            output_matrix.append(lst)

    img_printer(output_matrix)


