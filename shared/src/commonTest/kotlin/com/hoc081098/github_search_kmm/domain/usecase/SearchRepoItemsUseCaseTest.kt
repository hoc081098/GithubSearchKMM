package com.hoc081098.github_search_kmm.domain.usecase

import arrow.core.getOrHandle
import arrow.core.left
import arrow.core.right
import com.hoc081098.github_search_kmm.domain.model.AppError
import com.hoc081098.github_search_kmm.domain.repository.RepoItemRepository
import com.hoc081098.github_search_kmm.genRepoItems
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.once
import io.mockative.verify
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail
import kotlinx.coroutines.test.runTest

class SearchRepoItemsUseCaseTest {
  @Mock
  private lateinit var repoItemRepository: RepoItemRepository
  private lateinit var searchRepoItemsUseCase: SearchRepoItemsUseCase

  @BeforeTest
  fun setup() {
    repoItemRepository = mock(classOf())
    searchRepoItemsUseCase = SearchRepoItemsUseCase(repoItemRepository)
  }

  @AfterTest
  fun teardown() {
    verify(repoItemRepository).hasNoUnverifiedExpectations()
    verify(repoItemRepository).hasNoUnmetExpectations()
  }

  @Test
  fun `returns a Right WHEN RepoItemRepository returns a Right`() = runTest {
    val items = genRepoItems(0..10)
    val term = "term"
    val page = 1

    given(repoItemRepository)
      .coroutine { searchRepoItems(term, page) }
      .then { items.right() }

    val either = searchRepoItemsUseCase(term, page)

    assertEquals(
      items,
      either.getOrHandle { throw it }
    )
    verify(repoItemRepository)
      .coroutine { searchRepoItems(term, page) }
      .wasInvoked(exactly = once)
  }

  @Test
  fun `returns a Left WHEN RepoItemRepository returns a Left`() = runTest {
    val error = AppError.ApiException.NetworkException(null)
    val term = "term"
    val page = 1

    given(repoItemRepository)
      .coroutine { searchRepoItems(term, page) }
      .then { error.left() }

    val either = searchRepoItemsUseCase(term, page)

    assertEquals(
      error,
      either.fold(ifLeft = { it }, ifRight = { fail("Expected Left but got Right") })
    )
    verify(repoItemRepository)
      .coroutine { searchRepoItems(term, page) }
      .wasInvoked(exactly = once)
  }
}