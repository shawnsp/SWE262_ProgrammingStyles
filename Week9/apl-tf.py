import sys, string
import numpy as np
# Example input: "Hello  World!"
# characters = np.array([' ']+list(open(sys.argv[1]).read())+[' '])
characters = np.array([' ']+list(open("../pride-and-prejudice.txt").read())+[' '])
# Result: array([' ', 'H', 'e', 'l', 'l', 'o', ' ', ' ',
#           'W', 'o', 'r', 'l', 'd', '!', ' '], dtype='<U1')


# Normalize
characters[~np.char.isalpha(characters)] = ' '
characters = np.char.lower(characters)
# Result: array([' ', 'h', 'e', 'l', 'l', 'o', ' ', ' ',
#           'w', 'o', 'r', 'l', 'd', ' ', ' '], dtype='<U1')


### Split the words by finding the indices of spaces
sp = np.where(characters == ' ')
# Result: (array([ 0, 6, 7, 13, 14], dtype=int64),)


# A little trick: let's double each index, and then take pairs
sp2 = np.repeat(sp, 2)
# Result: array([ 0, 0, 6, 6, 7, 7, 13, 13, 14, 14], dtype=int64)


# Get the pairs as a 2D matrix, skip the first and the last
w_ranges = np.reshape(sp2[1:-1], (-1, 2))
# Result: array([[ 0,  6],
#                [ 6,  7],
#                [ 7, 13],
#                [13, 14]], dtype=int64)


# Remove the indexing to the spaces themselves
w_ranges = w_ranges[np.where(w_ranges[:, 1] - w_ranges[:, 0] > 2)]
# Result: array([[ 0,  6],
#                [ 7, 13]], dtype=int64)


# Voila! Words are in between spaces, given as pairs of indices
words = list(map(lambda r: characters[r[0]:r[1]], w_ranges))
# Result: [array([' ', 'h', 'e', 'l', 'l', 'o'], dtype='<U1'),
#          array([' ', 'w', 'o', 'r', 'l', 'd'], dtype='<U1')]


# Let's recode the characters as strings
swords = np.array(list(map(lambda w: ''.join(w).strip(), words)))
# Result: array(['hello', 'world'], dtype='<U5')


# Next, let's remove stop words
stop_words = np.array(list(set(open('../stop_words.txt').read().split(','))))
ns_words = swords[~np.isin(swords, stop_words)]
# Make all the non stopwords uppercase
filtered_words = [word.upper() for word in ns_words]


# create translation table
transTable = str.maketrans("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "48<D₤ƒ6#19K1MпØpQ®$+UVW%¥2")
# translate filtered_words to leet_words
leet_words = np.core.defchararray.translate(filtered_words, transTable, deletechars="None")
# create two gram
two_grams = np.stack((leet_words[:-1], leet_words[1:]), axis=1)


### Finally, count the word occurrences
uniq, counts = np.unique(two_grams, axis=0, return_counts=True)
wf_sorted = sorted(zip(uniq, counts), key=lambda t: t[1], reverse=True)


# prints out the 5 most frequently occurring 2-grams
for w, c in wf_sorted[:5]:
    print(w, '-', c)