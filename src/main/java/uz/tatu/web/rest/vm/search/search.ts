export default function buildSearchQueryOpts(searchForm) {
  if (searchForm) {
    let query = '';
    for (const idx of Object.keys(searchForm)) {
      if (searchForm[idx] && idx === 'page') {
        const page = searchForm[idx];
        if (page >= 1) {
          query = query + '&page=' + (page - 1);
        } else {
          query = query + '&page=' + page;
        }
      } else if (searchForm[idx] && idx === 'isMy' && searchForm[idx] === 'isNoMy') {
        query = query + '&isMyDocument=false';
      } else if (searchForm[idx] && idx !== 'sort') {
        query = query + '&' + idx + '=' + searchForm[idx];
      } else if ((idx === 'isDeleted' || idx === 'isDisabled') && searchForm[idx] !== null) {
        query = query + '&' + idx + '=' + searchForm[idx];
      }
    }

    let sorts = '';
    if (searchForm.propOrder && searchForm.reverse) {
      sorts += '&sort=' + searchForm.propOrder + ',' + searchForm.reverse;
    }
    query += `${sorts}`;
    return query;
  }
  return '';
}
