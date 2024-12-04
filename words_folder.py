def bucket_words(words: list, index: int):
    word = words[index]
    cnt = 1
    print(f"{word} ", end="")
    for idx in range(index + 1, len(words)):
        if word == words[idx]:
            cnt += 1
        else:
            if cnt != 1:
                print(f"[{cnt}]", end=" ")
            return idx
    return len(words)


def print_folded(words: list):
    idx = 0
    while True:
        idx = bucket_words(words, idx)
        if idx >= len(words):
            break


message = "hello hello world you are hero world world world hello hello again"
words = message.split(" ")


def bucketing_list(words: list):
    buckets = list()
    if words:
        idx = 0
        word = words[idx]
        cnt = 1
        idx += 1
        while idx < len(words):
            if words[idx] == word:
                cnt += 1
            else:
                buckets.append((word, cnt))
                word = words[idx]
                cnt = 1
            idx += 1
        buckets.append((word, cnt))
        return buckets

def print_buckets(buckets):
    for bucket in buckets:
        word, count = bucket
        if count > 1:
            print(f"{word} [{count}] ", end="")
        else:
            print(f"{word} ", end="")


bucketing_list(words=[])
bucketing_list(words="hello world".split(' '))
print_buckets(bucketing_list(words=words))
